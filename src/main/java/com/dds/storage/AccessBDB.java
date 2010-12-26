package com.dds.storage;

import java.io.File;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseException;

public class AccessBDB {

	public static void main(String[] args)
	{
		BDB exampleDbEnv = new BDB();

		try {
		    exampleDbEnv.setup(new File("store/"), true);
		    Database vendorDb = exampleDbEnv.getVendorDB();
		    
		    System.out.println(vendorDb.count());
		} catch(DatabaseException dbe) {
		    // Error code goes here
		} finally {
		    exampleDbEnv.close();
		} 
	}
}
