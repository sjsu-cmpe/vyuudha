package com.dds.server.nio;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import com.dds.storage.StorageHandler;
import com.dds.utils.Helper;

public class DDSWorker implements Runnable {
	private List<ServerDataEvent> queue = new LinkedList<ServerDataEvent>();

	public void processData(DDSServerNIO server, SocketChannel socket,
			byte[] data, int count) {
		byte[] dataCopy = new byte[count];
		System.arraycopy(data, 0, dataCopy, 0, count);

		dataCopy = Helper.getBytes(storageCall(dataCopy));
		synchronized (queue) {
			queue.add(new ServerDataEvent(server, socket, dataCopy));
			queue.notify();
		}
	}

	/**
	 * Makes a call to the DBRoot, Vyuudha storage abstraction engine
	 * 
	 * @param dataCopy
	 * @return object from the core storage layer
	 */
	private Object storageCall(byte[] dataCopy) {
		StorageHandler dbRoot = new StorageHandler();
		Object objectReturned = null;
		try {
			objectReturned = dbRoot.invoke(dataCopy);
			if (objectReturned != null) {
				return objectReturned;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objectReturned;
	}

	public void run() {
		ServerDataEvent dataEvent;

		while (true) {
			// Wait for data to become available
			synchronized (queue) {
				while (queue.isEmpty()) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
					}
				}
				dataEvent = (ServerDataEvent) queue.remove(0);
			}
			String out = (String) Helper.getObject(dataEvent.data);
			if (out != null) {
				// Return to sender
				dataEvent.server.send(dataEvent.socket, dataEvent.data);
			}
		}
	}
}