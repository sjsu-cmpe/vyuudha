package com.dds.storage;

import java.io.File;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class BDB {

	private Environment myEnv;
	private Database vendorDb;

	public BDB() {
	}

	public void setup(File envHome, boolean readOnly) throws DatabaseException {

		EnvironmentConfig myEnvConfig = new EnvironmentConfig();
		DatabaseConfig myDbConfig = new DatabaseConfig();
		myEnvConfig.setReadOnly(readOnly);
		myDbConfig.setReadOnly(readOnly);
		myEnvConfig.setAllowCreate(!readOnly);
		myDbConfig.setAllowCreate(!readOnly);
		myEnv = new Environment(envHome, myEnvConfig);

		// Now create and open our databases.
		vendorDb = myEnv.openDatabase(null, "VendorDB", myDbConfig);
	}

	// Getter methods
	public Environment getEnvironment() {
		return myEnv;
	}

	public Database getVendorDB() {
		return vendorDb;
	}

	// Close the environment
	public void close() {
		if (myEnv != null) {
			try {
				vendorDb.close();
				myEnv.close();
			} catch (DatabaseException dbe) {
				System.err.println("Error closing MyDbEnv: " + dbe.toString());
				System.exit(-1);
			}
		}
	}
}
