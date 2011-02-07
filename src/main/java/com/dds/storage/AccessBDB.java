package com.dds.storage;

import java.io.File;

import com.dds.storage.bdb.BDB;

public class AccessBDB {

	public static void main(String[] args)
	{
		BDB dbdEnv = null;
		try {
			StringBuilder path = new StringBuilder(new java.io.File(".").getCanonicalPath());
			path.append("/store/bdb/");

			File bdbPath = new File(path.toString());
			if (!bdbPath.exists()) {
			 	bdbPath.mkdirs();
		 	}
			
			BDB.setBdbPath(path.toString());
			dbdEnv = new BDB();
			dbdEnv.createConnection();
			
			//dbdEnv.put("key1", "value4");
			//dbdEnv.put("key2", "value2");
			System.out.println(dbdEnv.contains("key4"));
			System.out.println(dbdEnv.get("key2"));
					    
		} catch(Exception dbe) {
			System.out.println(dbe.toString());
		}
		finally {
			if (dbdEnv != null) {
				dbdEnv.closeConnection();
			}
		} 
	}
}
