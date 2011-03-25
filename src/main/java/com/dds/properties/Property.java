/**
 * 
 */
package com.dds.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	
	private String databasePropertyFile;
	private String replicationPropertyFile;
	private String serverConfigPropertyFile;
	
	private Map<String, String> databaseProperties = new HashMap<String, String>();
	private Map<String, String> replicationProperties = new HashMap<String, String>();
	private Map<String, String> serverConfigProperties = new HashMap<String, String>();
	
	private Property() {
		setDatabasePropertyFile();
		setReplicationPropertyFile();
		setServerConfigPropertyFile();
			
		setDatabaseProperties();
		setReplicationProperties();
		setServerConfigProperties();
	}
	
	public static Property getProperty() {
		return singleton;
	}

	/**
	 * @param databasePropertyFile the databasePropertyFile to set
	 * @throws IOException 
	 */
	private void setDatabasePropertyFile() {
		StringBuilder path;
		try {
			path = new StringBuilder(new java.io.File(".").getCanonicalPath());
			path.append("/config/database.properties");
			this.databasePropertyFile = path.toString();
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
			path.append("/config/server.properties");
			this.serverConfigPropertyFile = path.toString();
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
			path.append("/config/replication.properties");
			this.replicationPropertyFile = path.toString();
		} catch (IOException e) {
			logger.error("Exception : " + e.getMessage());
		}		
	}

	/**
	 * @return the serverConfigProperties
	 */
	public Map<String, String> getServerConfigProperties() {
		setServerConfigProperties();
		return serverConfigProperties;
	}

	/**
	 * @param serverConfigProperties the serverConfigProperties to set
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void setServerConfigProperties() {	
		try {
			FileInputStream fis = new FileInputStream(this.serverConfigPropertyFile);
			props.load(fis);
			if (this.serverConfigProperties.isEmpty() || 
					Boolean.parseBoolean(props.getProperty("update"))) {
				this.serverConfigProperties = generateMap(this.serverConfigPropertyFile);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			logger.error("Exception : " + e.getMessage());
		} catch (IOException e) {
			logger.error("Exception : " + e.getMessage());
		}
	}
		
	/**
	 * @return the databaseProperties
	 */
	public Map<String, String> getDatabaseProperties() {
		setDatabaseProperties();
		return databaseProperties;
	}


	/**
	 * @param databaseProperties the databaseProperties to set
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void setDatabaseProperties() {
		try {
			FileInputStream fis = new FileInputStream(this.databasePropertyFile);
			props.load(fis);
			if (this.databaseProperties.isEmpty() || 
					Boolean.parseBoolean(props.getProperty("update"))) {
				this.databaseProperties = generateMap(this.databasePropertyFile);
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
			FileInputStream fis = new FileInputStream(this.replicationPropertyFile);
			props.load(fis);
			if (this.databaseProperties.isEmpty() || 
					Boolean.parseBoolean(props.getProperty("update"))) {
				this.replicationProperties = generateMap(this.replicationPropertyFile);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			logger.error("Exception : " + e.getMessage());
		} catch (IOException e) {
			logger.error("Exception : " + e.getMessage());
		}		
	}
	
	public Map<String, String> getReplicationProperties() {
		setReplicationProperties();
		return replicationProperties;
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
        return this.props.containsKey(k);
    }
}
