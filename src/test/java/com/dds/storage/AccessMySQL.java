/**
 * 
 */
package com.dds.storage;

import com.dds.plugin.storage.mysql.MySQLDB;

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
