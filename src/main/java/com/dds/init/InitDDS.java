package com.dds.init;

import com.dds.interfaces.server.ServerInterface;
import com.dds.properties.Property;
import com.dds.server.bio.DDSServerBIO;
import com.dds.server.nio.DDSServerNIO;

public class InitDDS {
	
	String serverType;
	
	InitDDS() {
		setConfiguration();
	}
	
	/**
	 * This function should read the Server Type to
	 * initialize from a configuration file
	 */
	private void setConfiguration() {
		serverType = "NIO";
	}

	public static void main(String[] args) {
	
		InitDDS initDDS = new InitDDS();
		ServerInterface ddsIO;
		
		if (initDDS.serverType.equals("NIO")) {
			ddsIO = new DDSServerNIO();	
		} else  {  
			ddsIO = new DDSServerBIO();
		}
		ddsIO.start(null, 9090);
		System.out.println("Vyuudha " + initDDS.serverType + " Server Started...");
		System.out.println("Using " + Property.getProperty().getDatabaseProperties().get("dbToInstantiate"));
		
		// Starting JGroups channel
		//new ClusterCommunication().startCommunication();
		//System.out.println("JGroups channel established...");
	}
}