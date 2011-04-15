/**
 * 
 */
package com.dds.plugin.storage.bdb;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dds.utils.Property;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentLockedException;

/**
 * @author ravid
 * 
 */
public class BDBConfiguration {

	Logger logger = Logger.getLogger(BDBConfiguration.class);
	
	private boolean readOnly;
	private String store;
	private String bdbPath;
	private Environment myEnv;
	private File envHome;
	private EnvironmentConfig envConfig;
	private DatabaseConfig dbConfig;

	@SuppressWarnings("unused")
	private void setConfiguration() {
		setConfiguration(false);
	}
	
	private void setConfiguration(boolean replicate) {
		Map<String, String> properties;
		if (!replicate) {
			properties = Property.getProperty().getDatabaseProperties();
		} else {
			properties = Property.getProperty().getReplicationProperties();
		}
		
		setStore(properties.get("bdb_store"));
		setBdbPath(properties.get("bdb_path"));
		setEnvHome(this.bdbPath);
		setReadOnly(Boolean.parseBoolean(properties.get("bdb_readOnly")));
	}
	
	
	public Environment getConfiguration() {
		return getConfiguration(false);
	}
	
	public Environment getConfiguration(boolean replicate) {
		try {
			setConfiguration(replicate);
			setEnvConfig();
			setDbConfig();
			setEnvironment();
		} catch (EnvironmentLockedException e) {
			logger.error("Exception : " + e.getMessage());
		} catch (DatabaseException e) {
			logger.error("Exception : " + e.getMessage());
		}

		return this.myEnv;
	}

	/**
	 * @param store the store to set
	 */
	private void setStore(String store) {
		this.store = store;
	}

	/**
	 * @param bdbPath
	 *            the bdbPath to set
	 */
	private void setBdbPath(String bdbPath) {
		try {
			StringBuilder path;
			if (bdbPath == null || bdbPath.equals("")) {
				path = new StringBuilder(new java.io.File(".").getCanonicalPath());
			} else {
				bdbPath = bdbPath.endsWith("/") ? bdbPath : bdbPath.concat("/"); 
				path = new StringBuilder(bdbPath);
			}
			String store = this.store.startsWith("/") ? this.store.replaceFirst("/", "") : this.store;
			path.append(store);
			this.bdbPath = path.toString();
		} catch (IOException e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	/**
	 * @param envHome
	 *            the envHome to set
	 */
	private void setEnvHome(String envHome) {
		this.envHome = new File(envHome);
		if (!this.envHome.exists()) {
			this.envHome.mkdirs();
	 	}
	}

	/**
	 * @param readOnly
	 *            the readOnly to set
	 */
	private void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	private void setEnvironment() throws EnvironmentLockedException, DatabaseException {
		this.myEnv = new Environment(this.envHome, envConfig);
    }


	/**
	 * @return the myDbConfig
	 */
	public DatabaseConfig getDbConfig() {
		return dbConfig;
	}

	/**
	 * @param envConfig the envConfig to set
	 */
	private void setEnvConfig() {
		envConfig = new EnvironmentConfig();
		envConfig.setReadOnly(this.readOnly);
		envConfig.setAllowCreate(!this.readOnly);
	}

	/**
	 * @param dbConfig the dbConfig to set
	 */
	private void setDbConfig() {	
		dbConfig = new DatabaseConfig();
		dbConfig.setReadOnly(this.readOnly);
		dbConfig.setAllowCreate(!this.readOnly);
	}
}
