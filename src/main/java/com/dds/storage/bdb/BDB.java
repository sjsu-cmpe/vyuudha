package com.dds.storage.bdb;

import java.io.File;
import org.apache.log4j.Logger;

import com.dds.storage.DBInterface;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class BDB implements DBInterface{

	private Environment myEnv;
	private Database vendorDb;
	private Database database;
	Logger logger = Logger.getLogger(BDB.class);
	
	File envHome;
	boolean readOnly;
	
	/**
	 * @param readOnly the readOnly to set
	 */
	private void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public BDB() {
		//create folder in <project base>/store/part1
		setEnvHome(new File("store/part1/"));
		setReadOnly(false);
		database = getVendorDB();
	}
	
	/**
	 * @param envHome the envHome to set
	 */
	private void setEnvHome(File envHome) {
		this.envHome = envHome;
	}

	public void createConnection() {

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

	public String get(String key) {
		//database.get(null, key, null, null);
		return null;
	}

	public void put(String key, String value) {
		//database.put(null, key, value);
		
	}

	public void delete(String key) {
		// TODO Auto-generated method stub
		
	}

	public void closeConnection() {
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

	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return false;
	}
}
