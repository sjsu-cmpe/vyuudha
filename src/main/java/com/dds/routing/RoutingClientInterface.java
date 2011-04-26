/**
 * 
 */
package com.dds.routing;

/**
 * @author ravid
 *
 */
public interface RoutingClientInterface {
	
	public Object write(String stream) throws Exception; 
	//IOException, ClassNotFoundException;
	
	public void exit() throws Exception;
	
	public void initialize(String[] serverInfo) throws Exception;
	//UnknownHostException,	NumberFormatException, IOException, UnsupportedException;
}
