package com.dds.storage;

import com.dds.plugin.storage.bdb.BDB;

public class AccessBDB {

	public static void main(String[] args)
	{
		BDB dbdEnv = null;
		try {
		
			dbdEnv = new BDB();
			dbdEnv.createConnection();

			dbdEnv.put("key1", "value4");
			dbdEnv.put("key2", "value2");
			
			dbdEnv = null;
			dbdEnv = new BDB();
			dbdEnv.createConnection();
			System.out.println(dbdEnv.contains("key1"));
			System.out.println(dbdEnv.get("key1"));
					    
		} catch(Exception dbe) {
			dbe.printStackTrace();
		}
		finally {
			if (dbdEnv != null) {
				dbdEnv.closeConnection();
			}
		} 
	}
}
