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

/**
 * @author ravid
 *
 */
public class Property {

	private static Property singleton = new Property();
	Properties props = new Properties();
	
	private String databasePropertyFile;
	private String serverConfigPropertyFile;
	
	private Map<String, String> databaseProperties = new HashMap<String, String>();
	private Map<String, String> serverConfigProperties = new HashMap<String, String>();
	
	private Property() {
		setDatabasePropertyFile();
		setServerConfigPropertyFile();
			
		setDatabaseProperties();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			path.append("/config/server-config.properties");
			this.serverConfigPropertyFile = path.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			props.load(new FileInputStream(this.serverConfigPropertyFile));
			if (this.serverConfigProperties.isEmpty() || 
					Boolean.parseBoolean(props.getProperty("update"))) {
				this.serverConfigProperties = generateMap(this.serverConfigPropertyFile);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			props.load(new FileInputStream(this.databasePropertyFile));
			if (this.databaseProperties.isEmpty() || 
					Boolean.parseBoolean(props.getProperty("update"))) {
				this.databaseProperties = generateMap(this.databasePropertyFile);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
