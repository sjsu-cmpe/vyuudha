package com.dds.init;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.dds.cluster.Cluster;
import com.dds.interfaces.ServerInterface;
import com.dds.properties.Property;
import com.dds.server.bio.DDSServerBIO;
import com.dds.server.nio.DDSServerNIO;

public class InitDDS {
	
	private String serverType;
	private int serverPort;
	private String serverIp;
	
	InitDDS() {
		setConfiguration();
	}
	
	/**
	 * This function should read the Server Type to
	 * initialize from a configuration file
	 */
	private void setConfiguration() {
		Map<String, String> props = Property.getProperty().getServerConfigProperties();
		serverType = props.get("server_type");
		serverPort = Integer.parseInt(props.get("server_port"));
		serverIp = props.get("server_ip");
	}

	public static void main(String[] args) throws UnknownHostException {
	
		InitDDS initDDS = new InitDDS();
		ServerInterface ddsIO;
		
		if (initDDS.serverType.contains("NIO")) {
			ddsIO = new DDSServerNIO();	
		} else  {  
			ddsIO = new DDSServerBIO();
		}
		System.out.println("Vyuudha " + initDDS.serverType + " Server Started at " + initDDS.serverIp);
		System.out.println("Using " + Property.getProperty().getDatabaseProperties().get("dbToInstantiate"));

		InetAddress address = InetAddress.getByName(initDDS.serverIp);
		
		ddsIO.start(address, initDDS.serverPort);
		Cluster.setupCluster();		
	}
}