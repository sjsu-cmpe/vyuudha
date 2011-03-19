package com.dds.plugin.storage.bdb;

import java.util.List;

import org.apache.log4j.Logger;

import com.dds.exception.UnsupportedException;
import com.dds.interfaces.APIInterface;
import com.dds.utils.Helper;
import com.google.common.collect.Lists;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class BDB extends Database implements APIInterface{

	private static Environment myEnv;
	private Database vendorDb;
	
	Logger logger = Logger.getLogger(BDB.class);
	static BDBConfiguration bdbConfiguration = new BDBConfiguration();
	
	public BDB() {
		super(bdbConfiguration.getConfiguration());
	}

	public void createConnection() {

		try {
			myEnv = bdbConfiguration.getConfiguration();
			// Now create and open our databases.
			logger.info("Connection to BDB database established");
			vendorDb = myEnv.openDatabase(null, "VyuudhaDB", bdbConfiguration.getDbConfig());
			
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
			logger.error("Exception : " + e.getMessage());
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
			logger.error("Exception : " + e.getMessage());
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
			logger.error("Exception : " + e.getMessage());
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
	public Boolean contains(String key) {
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
			logger.error("Exception : " + e.getMessage());
		} 
		return keyFound;
	}
	
	public void createConnection(String bootstrapUrl) throws Exception {
		throw new UnsupportedException("Unsupported method");
	}
}
