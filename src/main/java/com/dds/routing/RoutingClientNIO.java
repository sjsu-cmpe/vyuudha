/**
 * 
 */
package com.dds.routing;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dds.exception.UnsupportedException;
import com.dds.utils.Helper;

/**
 * @author ravid
 *
 */
public class RoutingClientNIO implements RoutingClientInterface, Runnable {

	Logger logger = Logger.getLogger(RoutingClientNIO.class);
	
	private InetAddress host;
	private int port;
	private Selector selector;
	private ByteBuffer readBuffer = ByteBuffer.allocate(8192);
	private Thread t;
	private RspHandler handler;

	private List<RoutingChangeRequest> pendingChanges = new LinkedList<RoutingChangeRequest>();
	@SuppressWarnings("rawtypes")
	private Map<SocketChannel, List> pendingData = new HashMap<SocketChannel, List>();
	private Map<SocketChannel, RspHandler> rspHandlers = Collections.synchronizedMap(new HashMap<SocketChannel, RspHandler>());

	@Override
	public Object write(String stream) throws Exception {
		t = new Thread(this);
		t.setDaemon(true);
		t.start();
		handler = new RspHandler();
		send(Helper.getBytes(stream), handler);
		Object retObj = handler.waitForResponse();
		exit();
		return retObj;
	}

	@Override
	public void exit() throws Exception {
		handler = null;
		t = null;
	}

	@Override
	public void initialize(String[] serverInfo) throws Exception {
		if (serverInfo.length != 2) {
			throw new UnsupportedException("Insufficient parameters");
		}
		host = InetAddress.getByName(serverInfo[0]);
		port = Integer.parseInt(serverInfo[1]);
		initialize();

		logger.info("initialized");
		
	}
	
	private void initialize() throws Exception {
		
		selector = SelectorProvider.provider().openSelector();
	}

	public void run() {
		while (true) {
			try {
				// Process any pending changes
				synchronized (this.pendingChanges) {
					Iterator<RoutingChangeRequest> changes = this.pendingChanges.iterator();
					while (changes.hasNext()) {
						RoutingChangeRequest change = changes.next();
						switch (change.type) {
						case RoutingChangeRequest.CHANGEOPS:
							SelectionKey key = change.socket
									.keyFor(this.selector);
							key.interestOps(change.ops);
							break;
						case RoutingChangeRequest.REGISTER:
							change.socket.register(this.selector, change.ops);
							break;
						}
					}
					this.pendingChanges.clear();
				}

				// Wait for an event one of the registered channels
				this.selector.select();

				// Iterate over the set of keys for which events are available
				Iterator<?> selectedKeys = this.selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					// Check what event is available and deal with it
					if (key.isConnectable()) {
						this.finishConnection(key);
					} else if (key.isReadable()) {
						this.read(key);
					} else if (key.isWritable()) {
						this.write(key);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void send(byte[] data, RspHandler handler) throws IOException {

		SocketChannel socket = initiateConnection();
		rspHandlers.put(socket, handler);

		synchronized (this.pendingData) {
			List<ByteBuffer> queue = pendingData.get(socket);
			if (queue == null) {
				queue = new ArrayList<ByteBuffer>();
				this.pendingData.put(socket, queue);
			}
			queue.add(ByteBuffer.wrap(data));
		}

		this.selector.wakeup();
	}
	
	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		readBuffer.clear();

		int numRead;
		try {
			numRead = socketChannel.read(readBuffer);
		} catch (IOException e) {
			key.cancel();
			socketChannel.close();
			return;
		}

		if (numRead == -1) {
			key.channel().close();
			key.cancel();
			return;
		}

		this.handleResponse(socketChannel, this.readBuffer.array(), numRead);
	}

	private void handleResponse(SocketChannel socketChannel, byte[] data, int numRead) throws IOException {

		byte[] rspData = new byte[numRead];
		System.arraycopy(data, 0, rspData, 0, numRead);

		RspHandler handler = rspHandlers.get(socketChannel);


		if (handler.handleResponse(rspData)) {
			socketChannel.close();
			socketChannel.keyFor(this.selector).cancel();
		}
	}

	private void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		synchronized (this.pendingData) {
			List<?> queue = this.pendingData.get(socketChannel);

			// Write until there's not more data ...
			while (!queue.isEmpty()) {
				ByteBuffer buf = (ByteBuffer) queue.get(0);
				socketChannel.write(buf);
				if (buf.remaining() > 0) {
					break;
				}
				queue.remove(0);
			}

			if (queue.isEmpty()) {
				key.interestOps(SelectionKey.OP_READ);
			}
		}
	}

	private void finishConnection(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		try {
			socketChannel.finishConnect();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
			key.cancel();
			return;
		}

		key.interestOps(SelectionKey.OP_WRITE);
	}
	
	private SocketChannel initiateConnection() throws IOException {
		// Create a non-blocking socket channel
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);

		// Kick off connection establishment
		socketChannel.connect(new InetSocketAddress(host, port));
		
		// Queue a channel registration since the caller is not the
		// selecting thread. As part of the registration we'll register
		// an interest in connection events. These are raised when a channel
		// is ready to complete connection establishment.
		synchronized (pendingChanges) {
			pendingChanges.add(new RoutingChangeRequest(socketChannel, RoutingChangeRequest.REGISTER, SelectionKey.OP_CONNECT));
		}
		return socketChannel;
	}

}

class RoutingChangeRequest {
	public static final int REGISTER = 1;
	public static final int CHANGEOPS = 2;

	public SocketChannel socket;
	public int type;
	public int ops;

	public RoutingChangeRequest(SocketChannel socket, int type, int ops) {
		this.socket = socket;
		this.type = type;
		this.ops = ops;
	}
}


class RspHandler {
	private byte[] rsp = null;

	public synchronized boolean handleResponse(byte[] rsp) {
		this.rsp = rsp;
		this.notify();
		return true;
	}

	public synchronized Object waitForResponse() {
		while (this.rsp == null) {
			try {
				this.wait();
	 
			} catch (InterruptedException e) {
			}
		}
		
		return (Helper.getObject(this.rsp));
	}
}