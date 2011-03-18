package com.dds.cluster;

import java.util.ArrayList;
import java.util.Collection;

import com.dds.core.HashingContext;
import com.dds.interfaces.RoutingInterface;
import com.dds.plugin.routing.consistenthashing.ConsistentHashing;
import com.dds.utils.XMLConfigParser;

public class Cluster {

	private String clusterName = null;
	private int numberOfNodes = 0;
	Node node;
	Collection<Node> nodes = new ArrayList<Node>();
	
	public Cluster(String clusterName, int numberOfNodes){
		this.clusterName = clusterName;
		this.numberOfNodes = numberOfNodes;
	}
	
	public void setupCluster()
	{		
		//Create nodes collection
		nodes = XMLConfigParser.readNodes();
		
		//Select hashing algorithm
		HashingContext hash = new HashingContext();
		
		//Setup the routing strategy
		RoutingInterface ch = new ConsistentHashing(hash, 2, nodes);
		ch.setupRoutingCluster();
	}
	
	public String getclusterName()
	{
		return clusterName;
	}
	
	public int getnumberOfNodes()
	{
		return numberOfNodes;
	}
	
	public void setclusterName(String clusterName)
	{
		this.clusterName = clusterName;
	}
	
	public void setnumberOfNodes(int numberOfNodes)
	{
		this.numberOfNodes = numberOfNodes;
	}
	
}
