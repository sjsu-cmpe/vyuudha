/**
 * 
 */
package com.dds.plugin.storage.bdb;

import java.io.File;
import java.io.IOException;

import com.dds.exception.HandleException;
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

	private boolean readOnly;
	private String bdbPath;
	private Environment myEnv;
	private File envHome;
	private EnvironmentConfig envConfig;
	private DatabaseConfig dbConfig;

	private void setConfiguration() {
		// TODO Configurations to be read from file
		setBdbPath("");
		setEnvHome(getBdbPath());
		setReadOnly(false);
	}
	
	public Environment getConfiguration() {
		try {
			setConfiguration();
			setEnvConfig();
			setDbConfig();
			setEnvironment();
		} catch (EnvironmentLockedException e) {
			// TODO Auto-generated catch block
			HandleException.handler(e.getMessage(), 
					this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName());
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			HandleException.handler(e.getMessage(), 
					this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName());
		}

		return this.myEnv;
	}

	/**
	 * @param bdbPath
	 *            the bdbPath to set
	 */
	private void setBdbPath(String bdbPath) {
		StringBuilder path = new StringBuilder(bdbPath);
		if (!path.equals("EMPTY")) {
			try {
				path = new StringBuilder(new java.io.File(".").getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		path.append("/store/bdb/");
		this.bdbPath = path.toString();
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

		
		this.myEnv = new Environment(getEnvHome(), envConfig);
		
		
	}
	/**
	 * @return
	 */
	public Environment getEnvironment() {
		return myEnv;
	}

	/**
	 * @return the bdbPath
	 */
	private String getBdbPath() {
		return bdbPath;
	}

	/**
	 * @return the envHome
	 */
	private File getEnvHome() {
		return envHome;
	}

	/**
	 * @return
	 */
	private boolean getReadOnly() {
		return readOnly;
	}

	/**
	 * @return the envConfig
	 */
	public EnvironmentConfig getEnvConfig() {
		return envConfig;
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
		envConfig.setReadOnly(getReadOnly());
		envConfig.setAllowCreate(!getReadOnly());
	}

	/**
	 * @param dbConfig the dbConfig to set
	 */
	private void setDbConfig() {	
		dbConfig = new DatabaseConfig();
		dbConfig.setReadOnly(getReadOnly());
		dbConfig.setAllowCreate(!getReadOnly());
	}
}
