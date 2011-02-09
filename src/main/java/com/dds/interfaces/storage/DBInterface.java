/**
 * 
 */
package com.dds.interfaces.storage;

/**
 * Trying to work on this. Get a contract ready for what needs to be provided
 * to the core engine by the db api.
 * 
 * Basic skeleton, need to build on this.
 * 
 * @author ravid
 *
 */
public interface DBInterface {
	
	public void createConnection();
	
	public String get(String key); 
	
	public void put(String key, String value);
	
	public void delete(String key);
	
	public void closeConnection();
	
	public boolean contains(String key);
}
