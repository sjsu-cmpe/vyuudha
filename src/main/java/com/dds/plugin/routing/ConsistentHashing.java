package com.dds.plugin.routing;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.utils.Helper;
import com.dds.cluster.Node;

public class ConsistentHashing implements RoutingInterface {

	private HashingInterface hashFunction;
	private TreeMap<Integer, Node> circle = new TreeMap<Integer, Node>();
	
	public void setHashingTechnique(HashingInterface hashFunction)
	{
		this.hashFunction = hashFunction;
	}
	
	public void setupRoutingCluster(Collection<Node> nodes){
		for (Node node : nodes) {
			addNode(node);
		}
	}
	
	public void addNode(Node node) {
			circle.put(hashFunction.hash(node.toString().getBytes()), node); // Need to discuss
	}

	public void removeNode(Node node) {
			circle.remove(hashFunction.hash(node.toString().getBytes()));
	}

	public Node getNode(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = hashFunction.hash(Helper.getBytes(key));
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, Node> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}
}