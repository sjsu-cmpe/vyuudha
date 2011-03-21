package com.dds.core;

import java.util.Collection;

import com.dds.cluster.Node;
import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.plugin.routing.consistenthashing.ConsistentHashing;

/**
 * @author utkarsh
 *
 *This class implements the StateContext of state pattern.
 */
public class RoutingContext implements RoutingInterface{
	
	private RoutingInterface routingObjectState;

	public RoutingContext(HashingContext hash) {
		/*
		 * Routing goes hand in hand with Hashing algo
		 * So, when ever an object is created for Routing, Context will also create an object
		 * for hashing.
		 * Both of these object creation should be done in this manner:
		 * TO BE DONE in InitDDS.java
		 * 1. Read the server-config.properties
		 * 2. Instantiate the classes (Routing, Hashing, Membership) once using Reflection API
		 * 3. Keep the objects in memory
		 * 
		 * TO BE DONE HERE:
		 * 4. Get the objects here
		 */
		RoutingInterface ch = new ConsistentHashing();
		ch.setHashingTechnique(hash);
		
		//Make it read from config file
		setState(ch);
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
	public void setupRoutingCluster(Collection<Node> nodes) {
		this.routingObjectState.setupRoutingCluster(nodes);		
	}

	@Override
	public void setHashingTechnique(HashingInterface hashFunction) {
		this.routingObjectState.setHashingTechnique(hashFunction);		
	}
}
