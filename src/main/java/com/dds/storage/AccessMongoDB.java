/**
 * 
 */
package com.dds.storage;

/**
 * To test the functionality of MongoDB.class
 * 
 * @author ravid
 * 
 */
public class AccessMongoDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MongoDB mdb = new MongoDB();
		mdb.setDbName("newDB");
		mdb.setCollection("newCollection");

		mdb.createConnection();
		mdb.insert("key1", "value1");
		mdb.get("key1");
		mdb.remove("key1");
	}

}
