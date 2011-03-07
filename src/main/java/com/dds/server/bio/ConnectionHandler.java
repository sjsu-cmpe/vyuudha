package com.dds.server.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.dds.storage.DBRoot;
import com.dds.utils.Helper;

public class ConnectionHandler implements Runnable{
	private Socket socket;

	public ConnectionHandler(Socket socket) {
		this.socket = socket;

		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		try
		{
			//
			// Read a message sent by client application
			//
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String message = (String) ois.readObject();
			
			Object retVal = storageCall(Helper.getBytes(message));

			byte[] data = Helper.getBytes(retVal);

			//
			// Send a response information to the client application
			//
			if (retVal == null) {
				data = Helper.getBytes("Job Done");
			}
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(data);

			oos.close();
			ois.close();

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Makes a call to the DBRoot, Vyuudha storage abstraction engine
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
}
