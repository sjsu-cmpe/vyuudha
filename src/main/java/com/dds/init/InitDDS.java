package com.dds.init;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.dds.core.GlobalVariables;
import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.MembershipInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.interfaces.ServerInterface;
import com.dds.replication.ReplicationServerNIO;
import com.dds.utils.Property;
import com.dds.utils.XMLConfigParser;

public class InitDDS {
	
	public static void main(String[] args) throws UnknownHostException {
		if(args.length == 1) {
			//Read config folder and pass it on to start(String configPath)
			InitDDS.start(args[0]);
		} else	{
			//Use default config folder and pass it on to start(String configPath)
			InitDDS.start("config");
		}
	}
	
	public static void start(String configPath) throws UnknownHostException
	{
		//The new config folder has to be in the project root folder
		//eg. /home/ravid/Documents/git/vyuudha/newConfigFolder 
		GlobalVariables.INSTANCE.setProperties(configPath);
		Map<String, String> replicationMap = Property.getProperty().getReplicationProperties();
		
		//Create nodes collection
		GlobalVariables.INSTANCE.nodeList = XMLConfigParser.readNodes(configPath + "/nodes.xml");
		
		//Make property loading flexible here.

		//Get the hashing technique
		HashingInterface hash = GlobalVariables.INSTANCE.getHash();
		
		//Setup the routing strategy
		RoutingInterface routing = GlobalVariables.INSTANCE.getRouting();
		routing.setHashingTechnique(hash);
		routing.setupRoutingCluster(GlobalVariables.INSTANCE.nodeList);

		//Setup the membership
		MembershipInterface membership = GlobalVariables.INSTANCE.getMembership();
		membership.start();
		
		System.out.println("Vyuudha " + GlobalVariables.INSTANCE.getServerType() + " Server Started at " + GlobalVariables.INSTANCE.getServerIp());
		System.out.println("Using " + Property.getProperty().getDatabaseProperties().get("dbToInstantiate"));

		//Setup the server and start listening to request
		InetAddress address = InetAddress.getByName(GlobalVariables.INSTANCE.getServerIp());
		ServerInterface serverIO = GlobalVariables.INSTANCE.getServer();
		serverIO.start(address, GlobalVariables.INSTANCE.getServerPortExternal());
		
		//Setup replication server and start listening to requests
		InetAddress replicationAddress = InetAddress.getByName(replicationMap.get("server_ip"));
		ServerInterface replicationIO = new ReplicationServerNIO();
		int replicationPort = Integer.parseInt(replicationMap.get("server_port_internal"));
		replicationIO.start(replicationAddress, replicationPort);
	}
}
