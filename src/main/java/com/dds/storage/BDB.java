package com.dds.storage;

import java.io.File;
import org.apache.log4j.Logger;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class BDB {

	private Environment myEnv;
	private Database vendorDb;
	Logger logger = Logger.getLogger(BDB.class);

	public BDB() {
	}

	public void setup(File envHome, boolean readOnly) throws DatabaseException {

		logger.info("Getting into setup()");
		EnvironmentConfig myEnvConfig = new EnvironmentConfig();
		DatabaseConfig myDbConfig = new DatabaseConfig();

		myEnvConfig.setReadOnly(readOnly);
		myDbConfig.setReadOnly(readOnly);
		myEnvConfig.setAllowCreate(!readOnly);
		myDbConfig.setAllowCreate(!readOnly);
		try {
			myEnv = new Environment(envHome, myEnvConfig);
			// Now create and open our databases.
			logger.info("Create and open database");
			vendorDb = myEnv.openDatabase(null, "VendorDB", myDbConfig);
		} catch (Exception ex) {
			logger.info("Error in setup: " + ex.toString());
		}
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
		logger.info("Closing database");
		if (myEnv != null) {
			try {
				vendorDb.close();
				myEnv.close();
			} catch (DatabaseException dbe) {
				logger.info("Error in closing database: " + dbe.toString());
				System.exit(-1);
			}
		}
	}
}
