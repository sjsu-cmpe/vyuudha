package com.dds.init;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.dds.core.GlobalVariables;
import com.dds.core.ReplicationHandler;
import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.MembershipInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.interfaces.ServerInterface;
import com.dds.server.routing.RoutingServerNIO;
import com.dds.utils.Property;
import com.dds.utils.XMLConfigParser;

public class InitDDS {
	
	public static void main(String[] args) throws UnknownHostException {
		if(args.length == 1) {
			InitDDS.start(args[0]);
		} else	{
			InitDDS.start("config");
		}
	}
	
	public static void start(String configPath) throws UnknownHostException
	{
		//Create nodes collection
		GlobalVariables.INSTANCE.nodeList = XMLConfigParser.readNodes(configPath + "/nodes.xml");
		//The new config folder has to be in the project root folder
		//eg. If the config file is here: /home/ravid/Documents/git/vyuudha/newConfigFolder 
		// Write: newConfigFolder
		GlobalVariables.INSTANCE.setProperties(configPath);
				
		InetAddress address = InetAddress.getByName(GlobalVariables.INSTANCE.getServerIp());
		boolean singleInstance = GlobalVariables.INSTANCE.isSingleInstance();
		
		//Get the hashing technique
		HashingInterface hash = GlobalVariables.INSTANCE.getHash();
		
		//Setup the routing strategy
		RoutingInterface routing = GlobalVariables.INSTANCE.getRouting();
		routing.setHashingTechnique(hash);
		routing.setupRoutingCluster(GlobalVariables.INSTANCE.nodeList);

		//Setup the membership
		MembershipInterface membership = GlobalVariables.INSTANCE.getMembership();
		membership.start();
		
		if (!singleInstance) {
			startServices(address);
		}
		System.out.println("Vyuudha " + GlobalVariables.INSTANCE.getServerType() + " Server starting at " + GlobalVariables.INSTANCE.getServerIp());
		System.out.println("Using " + Property.getProperty().getDatabaseProperties().get("db"));
		
		//Setup the server and start listening to request
		ServerInterface serverIO = GlobalVariables.INSTANCE.getServer();
		serverIO.start(address, GlobalVariables.INSTANCE.getServerPortExternal());
	}

	/**
	 * @param address
	 * @param singleInstance
	 * @throws UnknownHostException
	 */
	private static void startServices(InetAddress address) 
	throws UnknownHostException {
		
		
		//Start Replication Server
		ReplicationHandler replicationHandler = new ReplicationHandler();
		replicationHandler.initReplicationServer();
		
		//Start Routing Server
		RoutingServerNIO routingServerNIO = new RoutingServerNIO();
		routingServerNIO.start(address, GlobalVariables.INSTANCE.getRoutingPort());
	}
}
