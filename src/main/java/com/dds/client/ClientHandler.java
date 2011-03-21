/**
 * 
 */
package com.dds.client;

import com.dds.exception.UnsupportedException;
import com.dds.interfaces.APIInterface;

/**
 * @author ravid
 *
 */
public class ClientHandler implements APIInterface {

	private DDSClientInterface client;	

	/* 
	 * Method not in use for Client API
	 * @use com.dds.interfaces.APIInterface#createConnection(java.lang.String)
	 */
	public void createConnection() throws UnsupportedException {
		throw new UnsupportedException("Unsupported Method");
	}
	
	/* (non-Javadoc)
	 * @see com.dds.interfaces.APIInterface#createConnection(java.lang.String)
	 */
	public void createConnection(String bootstrapUrl) throws Exception {
		String[] array = bootstrapUrl.trim().split(":");
		
		if (array[0].equalsIgnoreCase("nio")) {
			client = new DDSClientNIO();
		} else {
			client = new DDSClientBIO();
		}
    	String[] serverInfo = new String[2];
    	System.arraycopy(array, 1, serverInfo, 0, 2);
    	client.initialize(serverInfo);
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.APIInterface#get(java.lang.String)
	 */
	public String get(String key) throws Exception {
		
		return (String)sendMessage("get," + key);
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.APIInterface#put(java.lang.String, java.lang.String)
	 */
	public void put(String key, String value) throws Exception {	
		try {
			sendMessage("put," + key + "," + value);
		} catch (Exception e) {
			throw new Exception("Put failed");
		}
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.APIInterface#delete(java.lang.String)
	 */
	public void delete(String key) throws Exception {
		try {
			sendMessage("delete," + key);
		} catch (Exception e) {
			throw new Exception("Delete failed");
		}

	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.APIInterface#closeConnection()
	 */
	public void closeConnection() throws Exception {
		client.exit();
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.APIInterface#contains(java.lang.String)
	 */
	public Boolean contains(String key) throws Exception {
		return (Boolean)sendMessage("contains," + key);
	}
	
	private Object sendMessage(String stream) throws Exception {
		return client.write(stream);
	}
	
	public Object nativeAPI(String...args) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (String item : args) {
			sb.append(item);
		}
		return client.write(sb.toString());
	}
}
