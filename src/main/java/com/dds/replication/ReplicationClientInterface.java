/**
 * 
 */
package com.dds.replication;

/**
 * @author ravid
 *
 */
public interface ReplicationClientInterface {
	
	public Object write(String stream) throws Exception; 
	//IOException, ClassNotFoundException;
	
	public void exit() throws Exception;
	
	public void initialize(String[] serverInfo) throws Exception;
	//UnknownHostException,	NumberFormatException, IOException, UnsupportedException;
}
