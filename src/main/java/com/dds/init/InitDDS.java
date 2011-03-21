package com.dds.init;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.dds.core.GlobalVariables;
import com.dds.core.HashingContext;
import com.dds.core.MembershipContext;
import com.dds.core.RoutingContext;
import com.dds.core.ServerContext;
import com.dds.properties.Property;
import com.dds.utils.XMLConfigParser;

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
		
		//Create nodes collection
		GlobalVariables.INSTANCE.nodes = XMLConfigParser.readNodes();
		
		//Get the hashing technique
		GlobalVariables.INSTANCE.hash = new HashingContext();
		
		//Setup the routing strategy
		RoutingContext routing = new RoutingContext(GlobalVariables.INSTANCE.hash);
		routing.setupRoutingCluster(GlobalVariables.INSTANCE.nodes);
		
		//Setup the membership
		GlobalVariables.INSTANCE.membership = new MembershipContext();
		GlobalVariables.INSTANCE.membership.start();
		
		//Setup the server and start listening to request
		GlobalVariables.INSTANCE.server = new ServerContext();
		InetAddress address = InetAddress.getByName(initDDS.serverIp);
		
		System.out.println("Vyuudha " + initDDS.serverType + " Server Started at " + initDDS.serverIp);
		System.out.println("Using " + Property.getProperty().getDatabaseProperties().get("dbToInstantiate"));
		
		GlobalVariables.INSTANCE.server.start(address, initDDS.serverPort);
	}
}