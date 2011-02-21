package com.dds.server.nio;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import com.dds.storage.DBRoot;
import com.dds.utils.Helper;

public class DDSWorker implements Runnable {
	private List queue = new LinkedList();

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
	 * Makes a call to the DBRoot, vyuudha storage abstraction engine
	 * 
	 * @param dataCopy
	 * @return object from the core storage layer
	 */
	private Object storageCall(byte[] dataCopy) {
		DBRoot dbRoot = new DBRoot();
		Object objectReturned = null;
		try {
			objectReturned = dbRoot.invoke(dataCopy);
			if (objectReturned != null) {
				String retVal = (String) objectReturned;
				return objectReturned;
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO LOG!
			System.out.println("Could not fetch : " + e.getMessage());
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println("Key not found : " + e.getMessage());
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
			System.out.println("Value : " + out);
			// Return to sender
			dataEvent.server.send(dataEvent.socket, dataEvent.data);
		}
	}
}