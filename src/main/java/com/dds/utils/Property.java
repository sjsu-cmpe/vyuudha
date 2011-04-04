/**
 * 
 */
package com.dds.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author ravid
 *
 */
public class Property {

	Logger logger = Logger.getLogger(Property.class);

	private static Property singleton = new Property();
	Properties props = new Properties();

	private final String defaultConfigFolder = "/config/";
	private String configFolder;
	private String databasePropertyFile;
	private String replicationPropertyFile;
	private String serverConfigPropertyFile;

	private Map<String, String> databaseProperties = new HashMap<String, String>();
	private Map<String, String> replicationProperties = new HashMap<String, String>();
	private Map<String, String> serverConfigProperties = new HashMap<String, String>();

	private Property() {
		init();
	}

	public static Property getProperty() {
		return singleton;
	}

	private void init() {
		configFolder = defaultConfigFolder;
		setDatabasePropertyFile();
		setReplicationPropertyFile();
		setServerConfigPropertyFile();

		setDatabaseProperties();
		setReplicationProperties();
		setServerConfigProperties();
	}

	public void setConfigFolder(String folderName) {

		if (folderName == null) {
			if (!configFolder.equals(defaultConfigFolder)){
				databaseProperties.clear();
				serverConfigProperties.clear();
				replicationProperties.clear();
				init();
			}
			return;
		}
		folderName = folderName.contains("\\") ? folderName.replace('\\', ' ').trim() : folderName;
		folderName = folderName.contains("/") ? folderName.replace('/', ' ').trim() : folderName;

		configFolder = "/" + folderName + "/";
		
		databaseProperties.clear();
		serverConfigProperties.clear();
		replicationProperties.clear();

		setDatabasePropertyFile();
		setReplicationPropertyFile();
		setServerConfigPropertyFile();
	}
	/**
	 * @param databasePropertyFile the databasePropertyFile to set
	 * @throws IOException 
	 */
	private void setDatabasePropertyFile() {
		StringBuilder path;
		try {
			path = new StringBuilder(new java.io.File(".").getCanonicalPath());
			path.append(configFolder + "database.properties");
			databasePropertyFile = path.toString();
		} catch (IOException e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	/**
	 * @param serverConfigPropertyFile the serverConfigPropertyFile to set
	 * @throws IOException 
	 */
	private void setServerConfigPropertyFile() {
		StringBuilder path;
		try {
			path = new StringBuilder(new java.io.File(".").getCanonicalPath());
			path.append(configFolder + "server.properties");
			serverConfigPropertyFile = path.toString();
		} catch (IOException e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	/**
	 * 
	 */
	private void setReplicationPropertyFile() {
		StringBuilder path;
		try {
			path = new StringBuilder(new java.io.File(".").getCanonicalPath());
			path.append(configFolder + "replication.properties");
			replicationPropertyFile = path.toString();
		} catch (IOException e) {
			logger.error("Exception : " + e.getMessage());
		}		
	}

	/**
	 * @return the serverConfigProperties
	 */
	public Map<String, String> getServerConfigProperties() {
		return getServerConfigProperties(null);
	}

	/**
	 * @return the serverConfigProperties
	 */
	public Map<String, String> getServerConfigProperties(String folderName) {
		setConfigFolder(folderName);
		setServerConfigProperties();
		return Collections.unmodifiableMap(serverConfigProperties);	
	}

	/**
	 * @return the databaseProperties
	 */
	public Map<String, String> getDatabaseProperties() {
		return getDatabaseProperties(null);		
	}

	/**
	 * @return the databaseProperties
	 */
	public Map<String, String> getDatabaseProperties(String folderName) {
		setConfigFolder(folderName);
		setDatabaseProperties();
		return Collections.unmodifiableMap(databaseProperties);	
	}

	/**
	 * @return
	 */
	public Map<String, String> getReplicationProperties() {
		return getReplicationProperties(null);
	}

	/**
	 * @return
	 */
	public Map<String, String> getReplicationProperties(String folderName) {
		setConfigFolder(folderName);
		setReplicationProperties();
		return Collections.unmodifiableMap(replicationProperties);		
	}

	/**
	 * @param serverConfigProperties the serverConfigProperties to set
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void setServerConfigProperties() {	
		try {
			FileInputStream fis = new FileInputStream(serverConfigPropertyFile);
			props.load(fis);
			if (serverConfigProperties.isEmpty() || 
					Boolean.parseBoolean(props.getProperty("update"))) {
				serverConfigProperties = generateMap(serverConfigPropertyFile);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			logger.error("Exception : " + e.getMessage());
		} catch (IOException e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	/**
	 * @param databaseProperties the databaseProperties to set
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void setDatabaseProperties() {
		try {
			FileInputStream fis = new FileInputStream(databasePropertyFile);
			props.load(fis);
			if (databaseProperties.isEmpty() || 
					Boolean.parseBoolean(props.getProperty("update"))) {
				databaseProperties = generateMap(databasePropertyFile);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			logger.error("Exception : " + e.getMessage());
		} catch (IOException e) {
			logger.error("Exception : " + e.getMessage());
		}
	}

	private void setReplicationProperties() {
		try {
			FileInputStream fis = new FileInputStream(replicationPropertyFile);
			props.load(fis);
			if (databaseProperties.isEmpty() || 
					Boolean.parseBoolean(props.getProperty("update"))) {
				replicationProperties = generateMap(replicationPropertyFile);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			logger.error("Exception : " + e.getMessage());
		} catch (IOException e) {
			logger.error("Exception : " + e.getMessage());
		}		
	}

	private Map<String, String> generateMap(String file) throws FileNotFoundException, IOException {
		//props.load(new FileInputStream(file));
		Map<String, String> map = new HashMap<String, String>();
		Set<Object> keys = props.keySet();
		for (Object key : keys) {
			map.put(key.toString(), props.getProperty(key.toString()));
		}
		return map;
	}	

	public boolean containsKey(Object k) {
		return props.containsKey(k);
	}
}
