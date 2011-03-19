/**
 * 
 */
package com.dds.client.interfaces;

/**
 * @author ravid
 *
 */
public interface DDSClientInterface {
	
	public Object write(String stream) throws Exception; 
	//IOException, ClassNotFoundException;
	
	public void exit() throws Exception;
	
	public void initialize(String[] serverInfo) throws Exception;
	//UnknownHostException,	NumberFormatException, IOException, UnsupportedException;
	
	

}
