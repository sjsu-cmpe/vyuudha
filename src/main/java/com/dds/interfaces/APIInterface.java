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
public interface APIInterface {
	
	public void createConnection() throws Exception;
	
	/**
	 * Used to create connection with the storage engine
	 */
	public void createConnection(String bootstrapUrl) throws Exception;
	
	/**
	 * Get a key from the storage and return to calling method.
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public String get(String key) throws Exception; 
	
	/**
	 * Put a key into storage
	 * @param key
	 * @param value
	 * @throws Exception 
	 */
	public void put(String key, String value) throws Exception;
	
	/**
	 * Delete a key from the storage 
	 * @param key
	 * @throws Exception 
	 */
	public void delete(String key) throws Exception;
	
	/**
	 * Close the connection with the storage api
	 * @throws Exception 
	 */
	public void closeConnection() throws Exception;
	
	/**
	 * Checks if storage contains the given key or not
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public Boolean contains(String key) throws Exception;
}
