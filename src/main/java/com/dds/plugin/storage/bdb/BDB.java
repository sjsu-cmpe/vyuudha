package com.dds.plugin.storage.bdb;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dds.exception.UnsupportedException;
import com.dds.interfaces.APIInterface;
import com.dds.utils.Helper;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class BDB extends Database implements APIInterface{

	private static boolean openConnection = false;
	private static Environment myEnv;
	private static Database vendorDb;

	Logger logger = Logger.getLogger(BDB.class);
	static BDBConfiguration bdbConfiguration = new BDBConfiguration();

	public BDB() {
		super(bdbConfiguration.getConfiguration());
	}

	public BDB(boolean replicate) {
		super(bdbConfiguration.getConfiguration(true));
	}

	public void createConnection() {

		try {
			if (!openConnection) {
				if (myEnv == null || vendorDb == null) {
					myEnv = bdbConfiguration.getConfiguration();
					// Now create and open our databases.
					logger.info("Connection to BDB database established");
					vendorDb = myEnv.openDatabase(null, bdbConfiguration.getDbName(), bdbConfiguration.getDbConfig());
				}
				openConnection = true;
				logger.info("BDB connection created");
			}

		} catch (Exception ex) {
			logger.info("Error in setup: " + ex.toString());
		}
		logger.info("BDB connection created111");
	}

	// Getter methods
	public Environment getEnvironment() {
		return myEnv;
	}

	public Database getVendorDB() {
		return vendorDb;
	}

	public String get(String key) {
		return get(key, false);
	}
	/* (non-Javadoc)
	 * @see com.dds.interfaces.storage.DBInterface#get(java.lang.String)
	 */
	public String get(String key, boolean replicate) {
		checkVendorDB();
		
		if (!contains(key) && !replicate) {
			Object retObj = getFromReplicate(key);
			return (String)retObj;
		}

		DatabaseEntry entryKey = new DatabaseEntry(Helper.getBytes(key));

		Cursor cursor = null;
		DatabaseEntry entryValue = new DatabaseEntry();
		List<Object> results = new ArrayList<Object>();
		LockMode lockMode = LockMode.DEFAULT;
		try {
			cursor = vendorDb.openCursor(null, null);
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
		retObj = retObj == null ? "Not Found" : retObj;

		return (String) retObj;
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.storage.DBInterface#put(java.lang.String, java.lang.String)
	 */
	public void put(String key, String value) {
		checkVendorDB();
		DatabaseEntry entryKey = new DatabaseEntry(Helper.getBytes(key));
		DatabaseEntry entryValue = new DatabaseEntry(Helper.getBytes(value));

		try {
			vendorDb.put(null, entryKey, entryValue);
		} catch (DatabaseException e) {
			logger.error("Exception : " + e.getMessage());
		}		
		logger.info(key + " : " + value + " inserted into DB");
	}

	private void checkVendorDB() {
		if (vendorDb == null) {
			openConnection = false;
			this.createConnection();
		}
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.storage.DBInterface#delete(java.lang.String)
	 */
	public void delete(String key) {
		checkVendorDB();
		DatabaseEntry entryKey = new DatabaseEntry(Helper.getBytes(key));

		try {
			OperationStatus status = vendorDb.delete(null, entryKey);
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
		openConnection = false;
//		if (myEnv != null || vendorDb != null) {
//			try {		
//				vendorDb.close();
//				myEnv.close();
//			} catch (DatabaseException dbe) {
//				logger.info("Error in closing database: " + dbe.toString());
//			}
//			vendorDb = null;
//			myEnv = null;
//		}
//		logger.info("BDB connection closed");
	}

	/* (non-Javadoc)
	 * @see com.dds.interfaces.storage.DBInterface#contains(java.lang.String)
	 */
	public Boolean contains(String key) {
		checkVendorDB();

		DatabaseEntry entryKey = new DatabaseEntry(Helper.getBytes(key));

		boolean keyFound = false;
		Cursor cursor = null;
		DatabaseEntry entryValue = new DatabaseEntry();

		try {
			cursor = vendorDb.openCursor(null, null);
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

	public Object nativeAPI(String... args) throws Exception {
		throw new UnsupportedException("Unsupported method");
	}

	@Override
	public void replicate(String key, String value, int factor)
	throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public void replicate(String key, String value) throws Exception {
		logger.info("Replicate function");
		BDB bdb = new BDB(true);
		bdb.createConnection();
		bdb.put(key, value);
		bdb.closeConnection();
		bdb = null;
	}

	public Object getFromReplicate(String key) {
		logger.info("Getting from replicate store");
		BDB bdb = new BDB(true);
		bdb.createConnection();

		Object retObj = bdb.get(key, true);	
		bdb.closeConnection();
		return (String) retObj;
	}
}