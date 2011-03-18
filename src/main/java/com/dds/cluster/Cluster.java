package com.dds.cluster;

import java.util.ArrayList;
import java.util.Collection;

import com.dds.core.HashingContext;
import com.dds.core.RoutingContext;
import com.dds.utils.XMLConfigParser;

public class Cluster {
	Node node;
	static Collection<Node> nodes = new ArrayList<Node>();
	
	
	public static void setupCluster()
	{		
		//Create nodes collection
		nodes = XMLConfigParser.readNodes();
		
		//Select hashing algorithm
		HashingContext hash = new HashingContext();
		
		//Setup the routing strategy
		RoutingContext routing = new RoutingContext(hash, 2, nodes);
		routing.setupRoutingCluster();
	}
}
