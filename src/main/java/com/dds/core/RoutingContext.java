package com.dds.core;

import java.util.Collection;

import com.dds.cluster.Node;
import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.plugin.routing.consistenthashing.ConsistentHashing;

public class RoutingContext implements RoutingInterface{
	
	private RoutingInterface routingObjectState;

	public RoutingContext(
			HashingInterface hashFunction, 
			int numberOfReplicas, 
			Collection<Node> nodes) {
		 setState(new ConsistentHashing(hashFunction, numberOfReplicas, nodes)); //Make it read from config file
	}

	public void setState(RoutingInterface newRoutingObjectState) {
		this.routingObjectState = newRoutingObjectState;
	}

	@Override
	public void addNode(Node node) {
		this.routingObjectState.addNode(node);
	}

	@Override
	public void removeNode(Node node) {
		this.routingObjectState.removeNode(node);
	}

	@Override
	public Node getNode(Object key) {
		return this.routingObjectState.getNode(key);
	}

	@Override
	public void setupRoutingCluster() {
		this.routingObjectState.setupRoutingCluster();		
	}
}
