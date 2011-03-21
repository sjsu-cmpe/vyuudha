package com.dds.plugin.routing.vbuckets;

import java.util.Collection;

import com.dds.cluster.Node;
import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.RoutingInterface;

/*
 * Inspired from membase. Need to work on the alog.
 */
public class VBuckets implements RoutingInterface{

	@Override
	public void addNode(Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeNode(Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node getNode(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setupRoutingCluster(Collection<Node> nodes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHashingTechnique(HashingInterface hashFunction) {
		// TODO Auto-generated method stub
		
	}
}
