package com.dds.storage;

import com.dds.storage.bdb.BDB;

public class AccessBDB {

	public static void main(String[] args)
	{
		BDB dbdEnv = new BDB();

		try {
			
			dbdEnv.createConnection();

		    
		} catch(Exception dbe) {
			System.out.println(dbe.toString());
		}
		finally {
			dbdEnv.closeConnection();
		} 
	}
}
