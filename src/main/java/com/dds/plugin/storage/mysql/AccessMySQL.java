/**
 * 
 */
package com.dds.plugin.storage.mysql;

/**
 * @author ravid
 *
 */
public class AccessMySQL {
	public static void main(String[] args) { 
		MySQLDB mySQLDB = new MySQLDB();
		
		mySQLDB.createConnection();
	}
}
