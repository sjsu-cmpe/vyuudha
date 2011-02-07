package com.dds.storage.bdb;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.dds.storage.DBInterface;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.dds.utils.Helper;
import com.google.common.collect.Lists;

public class BDB implements DBInterface{

	private static String bdbPath;
	private static Environment myEnv;
	private Database vendorDb;
	
	Logger logger = Logger.getLogger(BDB.class);
	private final Helper helper;
	
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
		setBdbPath(bdbPath);
		setEnvHome(new File(bdbPath));
		setReadOnly(false);
		helper = new Helper();
	}
	
	/**
	 * @param bdbPath the bdbPath to set
	 */
	public static void setBdbPath(String _bdbPath) {
		bdbPath = _bdbPath;
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
		
		DatabaseEntry entryKey = new DatabaseEntry(helper.getBytes(key));
		
		Cursor cursor = null;
        DatabaseEntry entryValue = new DatabaseEntry();
        List<Object> results = Lists.newArrayList();
        LockMode lockMode = LockMode.DEFAULT;
		try {
			cursor = getVendorDB().openCursor(null, null);
	        for(OperationStatus status = cursor.getSearchKey(entryKey, entryValue, lockMode); 
	        status == OperationStatus.SUCCESS; status = cursor.getNextDup(entryKey, entryValue, lockMode)) {
	        	results.add(helper.getObject(entryValue.getData()));
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
		
		return (String) retObj;
	}

	public void put(String key, String value) {
		DatabaseEntry entryKey = new DatabaseEntry(helper.getBytes(key));
		DatabaseEntry entryValue = new DatabaseEntry(helper.getBytes(value));
		
		try {
			getVendorDB().put(null, entryKey, entryValue);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		logger.info("Key " + key + " inserted!");
	}

	public void delete(String key) {
		DatabaseEntry entryKey = new DatabaseEntry(helper.getBytes(key));
		
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
	}

	public void closeConnection() {
		logger.info("Closing database");
		if (myEnv != null) {
			try {		
				getVendorDB().close();
				getEnvironment().close();
			} catch (DatabaseException dbe) {
				logger.info("Error in closing database: " + dbe.toString());
				System.exit(-1);
			}
		}		
	}

	public boolean contains(String key) {
		DatabaseEntry entryKey = new DatabaseEntry(helper.getBytes(key));
		
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
