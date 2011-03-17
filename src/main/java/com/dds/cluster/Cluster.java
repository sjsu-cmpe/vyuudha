package com.dds.cluster;

import java.util.ArrayList;
import java.util.Collection;

import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.plugin.hashing.MurmurHashFunction;
import com.dds.plugin.routing.consistenthashing.ConsistentHashing;

public class Cluster {

	private String clusterName = null;
	private int numberOfNodes = 0;
	Node node;
	
	public Cluster(String clusterName, int numberOfNodes){
		this.clusterName = clusterName;
		this.numberOfNodes = numberOfNodes;
	}
	
	public void setupCluster()
	{
		//Select hashing algorithm
		HashingInterface mhf = new MurmurHashFunction();
		
		//Create nodes
		//Get nodes info here from nodes.json
		Collection<Node> nodes = new ArrayList<Node>();
		for(int i=0; i<numberOfNodes; i++)
		{
			node = new Node(numberOfNodes, clusterName, numberOfNodes, numberOfNodes);
			nodes.add(node);
		}
		
		//Setup the routing strategy
		RoutingInterface ch = new ConsistentHashing(mhf, 2, nodes);
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
