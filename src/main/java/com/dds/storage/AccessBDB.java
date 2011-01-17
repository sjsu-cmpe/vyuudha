package com.dds.storage;

import java.io.File;

import com.sleepycat.je.Database;

public class AccessBDB {

	public static void main(String[] args)
	{
		BDB dbdEnv = new BDB();

		try {
			dbdEnv.setup(new File("store/part1/"), false);
		    Database database = dbdEnv.getVendorDB();
		    
		    database.put(null, null, null);
		    database.get(null, null, null, null);
		    
		} catch(Exception dbe) {
			System.out.println(dbe.toString());
		}
		finally {
			dbdEnv.close();
		} 
	}
}
