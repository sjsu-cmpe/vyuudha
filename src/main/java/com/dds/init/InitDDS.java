package com.dds.init;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.dds.core.GlobalVariables;
import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.MembershipInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.interfaces.ServerInterface;
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
		GlobalVariables.INSTANCE.nodeList = XMLConfigParser.readNodes();

		//Get the hashing technique
		//GlobalVariables.INSTANCE.hash = new HashingContext();

		HashingInterface hash = GlobalVariables.INSTANCE.getHash();
		//Setup the routing strategy
		//RoutingContext routing1 = new RoutingContext(GlobalVariables.INSTANCE.hash);
		RoutingInterface routing = GlobalVariables.INSTANCE.getRouting();
		routing.setHashingTechnique(hash);
		routing.setupRoutingCluster(GlobalVariables.INSTANCE.nodeList);

		//Setup the membership
		//GlobalVariables.INSTANCE.membership = new MembershipContext();
		//GlobalVariables.INSTANCE.membership.start();
		MembershipInterface membership = GlobalVariables.INSTANCE.getMembership();
		membership.start();
		
		System.out.println("Vyuudha " + initDDS.serverType + " Server Started at " + initDDS.serverIp);
		System.out.println("Using " + Property.getProperty().getDatabaseProperties().get("dbToInstantiate"));

		//Setup the server and start listening to request
		InetAddress address = InetAddress.getByName(initDDS.serverIp);
		ServerInterface serverIO = GlobalVariables.INSTANCE.getServer();
		serverIO.start(address, initDDS.serverPort);
	}
}
