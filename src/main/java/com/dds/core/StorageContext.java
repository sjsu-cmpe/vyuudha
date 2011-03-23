package com.dds.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dds.interfaces.APIInterface;
import com.dds.plugin.storage.bdb.BDB;
import com.dds.plugin.storage.mongodb.MongoDB;
import com.dds.plugin.storage.mysqldb.MySQLDB;


public class StorageContext {
	Logger logger = Logger.getLogger(StorageContext.class);

	Map<String, APIInterface> storageRegistry = new HashMap<String, APIInterface>();

	public StorageContext() {
		
	}

	/**
	 * @return the storageRegistry
	 */
	public Map<String, APIInterface> getStorageRegistry() {
		return storageRegistry;
	}

	/**
	 * @param storageRegistry the storageRegistry to set
	 */
	public void setStorageRegistry(String dbToInstantiate) {
		try {
			if (dbToInstantiate.equals("BDB")) {
				storageRegistry.put("BDB", new BDB());
			} else if (dbToInstantiate.equals("MySQLDB")) {
				storageRegistry.put("MySQLDB", new MySQLDB());
			} else if (dbToInstantiate.equals("MongoDB")) {
				storageRegistry.put("MongoDB", new MongoDB());
			} 
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}




}
