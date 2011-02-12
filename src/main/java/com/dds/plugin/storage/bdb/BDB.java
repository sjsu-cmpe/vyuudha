package com.dds.plugin.storage.bdb;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.dds.interfaces.storage.DBInterface;
import com.dds.utils.Helper;
import com.google.common.collect.Lists;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class BDB implements DBInterface{

	private static String bdbPath;
	private static Environment myEnv;
	private Database vendorDb;
	
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
		setConfiguration();
	}
	
	/**
	 * Used to set the configuration for BDB. Ideally this function
	 * should read from a properties file
	 * 
	 */
	private void setConfiguration() {
		// TODO Configurations to be read from file
		bdbPath = getBdbPath();
		setBdbPath(bdbPath);
		setEnvHome(new File(bdbPath));
		setReadOnly(false);
	}

	/**
	 * @param bdbPath the bdbPath to set
	 */
	public static void setBdbPath(String _bdbPath) {
		bdbPath = _bdbPath;
	}

	/**
	 * @return the bdbPath
	 */
	public static String getBdbPath() {
		StringBuilder path = new StringBuilder("");
		try {
			path = new StringBuilder(new java.io.File(".").getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		path.append("/store/bdb/");

		File bdbPath = new File(path.toString());
		if (!bdbPath.exists()) {
		 	bdbPath.mkdirs();
	 	}

		return path.toString();
	}

	/**
	 * @param envHome the envHome to set
	 */
	private void setEnvHome(File envHome) {
		this.envHome = envHome;
	}

	public void createConnection() {

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
		
		logger.info("BDB connection created");
	}

	// Getter methods
	public Environment getEnvironment() {
		return myEnv;
	}

	public Database getVendorDB() {
		return vendorDb;
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.storage.DBInterface#get(java.lang.String)
	 */
	public String get(String key) {
		
		DatabaseEntry entryKey = new DatabaseEntry(Helper.getBytes(key));
		
		Cursor cursor = null;
        DatabaseEntry entryValue = new DatabaseEntry();
        List<Object> results = Lists.newArrayList();
        LockMode lockMode = LockMode.DEFAULT;
		try {
			cursor = getVendorDB().openCursor(null, null);
	        for(OperationStatus status = cursor.getSearchKey(entryKey, entryValue, lockMode); 
	        status == OperationStatus.SUCCESS; status = cursor.getNextDup(entryKey, entryValue, lockMode)) {
	        	results.add(Helper.getObject(entryValue.getData()));
	        }
	        cursor.close();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		Object retObj = null;
		for (Object obj : results)	{
			retObj = obj;
		}
		String value = (String) retObj;
		logger.info(key + " : " + value + " retrieved from DB");
		return (String) retObj;
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.storage.DBInterface#put(java.lang.String, java.lang.String)
	 */
	public void put(String key, String value) {
		DatabaseEntry entryKey = new DatabaseEntry(Helper.getBytes(key));
		DatabaseEntry entryValue = new DatabaseEntry(Helper.getBytes(value));
		
		try {
			getVendorDB().put(null, entryKey, entryValue);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		logger.info(key + " : " + value + " inserted into DB");
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.storage.DBInterface#delete(java.lang.String)
	 */
	public void delete(String key) {
		DatabaseEntry entryKey = new DatabaseEntry(Helper.getBytes(key));
		
		try {
			OperationStatus status = getVendorDB().delete(null, entryKey);
			if (status == OperationStatus.SUCCESS) {
				logger.info("Key " + key + " deleted");
			} else {
				logger.info("Key " + key + " not found");
			}			
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info(key + " deleted from DB");
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.storage.DBInterface#closeConnection()
	 */
	public void closeConnection() {
		if (myEnv != null) {
			try {		
				getVendorDB().close();
				getEnvironment().close();
			} catch (DatabaseException dbe) {
				logger.info("Error in closing database: " + dbe.toString());
				System.exit(-1);
			}
		}
		logger.info("BDB connection closed");
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.storage.DBInterface#contains(java.lang.String)
	 */
	public boolean contains(String key) {
		DatabaseEntry entryKey = new DatabaseEntry(Helper.getBytes(key));
		
		boolean keyFound = false;
		Cursor cursor = null;
        DatabaseEntry entryValue = new DatabaseEntry();

		try {
			cursor = getVendorDB().openCursor(null, null);
	        OperationStatus status = cursor.getSearchKey(entryKey, entryValue, LockMode.DEFAULT); 
	        if (status == OperationStatus.SUCCESS) {
	        	keyFound = true;
	        }
	        cursor.close();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return keyFound;
	}
}
