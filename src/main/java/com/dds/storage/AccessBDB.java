package com.dds.storage;

import java.io.File;

import com.sleepycat.je.Database;

public class AccessBDB {

	public static void main(String[] args)
	{
		BDB exampleDbEnv = new BDB();

		try {
		    exampleDbEnv.setup(new File("store/part1/"), false);
		    Database vendorDb = exampleDbEnv.getVendorDB();
		    
		    System.out.println(vendorDb.count());
		} catch(Exception dbe) {
			System.out.println(dbe.toString());
		}
		finally {
		    exampleDbEnv.close();
		} 
	}
}
