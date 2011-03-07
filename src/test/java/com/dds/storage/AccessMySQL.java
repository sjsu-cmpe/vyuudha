/**
 * 
 */
package com.dds.storage;

import com.dds.plugin.storage.mysqldb.MySQLDB;

/**
 * @author ravid
 *
 */
public class AccessMySQL {
	public static void main(String[] args) { 
		MySQLDB mySQLDB = new MySQLDB();
		
		mySQLDB.createConnection();
		
		mySQLDB.put("yellow1", "hehaheha");
		String result;
		result = mySQLDB.get("yellow");
		System.out.println("Value " + result);
//		mySQLDB.put("yellow", "stoned");
//		result = mySQLDB.get("yellow");
//		System.out.println("Value " + result);
//		//mySQLDB.delete("yellow");
//		result = mySQLDB.get("yellow");
//		System.out.println("Value " + result);
		
		mySQLDB.closeConnection();
	}
}
