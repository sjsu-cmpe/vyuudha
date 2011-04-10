/**
 * 
 */
package com.dds.replication;

import com.dds.exception.UnsupportedException;
import com.dds.interfaces.APIInterface;

/**
 * @author ravid
 *
 */
public class ReplicationClientHandler implements APIInterface {

	private ReplicationClientNIO client;	

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
		
		client = new ReplicationClientNIO();
		
     	client.initialize(array);
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.APIInterface#put(java.lang.String, java.lang.String)
	 */
	public void replicate(String key, String value, int factor) throws Exception {	
		try {
			sendMessage("replicate," + key + "," + value + "," + factor);
		} catch (Exception e) {
			throw new Exception("Replicate failed");
		}
	}

	private Object sendMessage(String stream) throws Exception {
		return client.write(stream);
	}
	
	@Override
	public String get(String key) throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public void put(String key, String value) throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public void delete(String key) throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public void closeConnection() throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public Boolean contains(String key) throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public Object nativeAPI(String... args) throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public void replicate(String key, String value) throws Exception {
		throw new UnsupportedException("Unsupported Method");	}
}
