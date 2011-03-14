/**
 * 
 */
package com.dds.interfaces;



/**
 * This is the vyuudha contract with the underlying storage plugins
 * All pluggable stroage api's should implement the interface and 
 * the functions.
 * 
 * TODO: Basic skeleton, need to build on this.
 * TODO: Make all String variables Object variables.
 * @author ravid
 *
 */
public interface DBInterface {
	
	/**
	 * Used to create connection with the storage engine
	 */
	public void createConnection();
	
	/**
	 * Get a key from the storage and return to calling method.
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key); 
	
	/**
	 * Put a key into storage
	 * @param key
	 * @param value
	 */
	public void put(String key, String value);
	
	/**
	 * Delete a key from the storage 
	 * @param key
	 */
	public void delete(String key);
	
	/**
	 * Close the connection with the storage api
	 */
	public void closeConnection();
	
	/**
	 * Checks if storage contains the given key or not
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(String key);
}
