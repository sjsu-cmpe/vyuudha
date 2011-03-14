package com.dds.plugin.routing.consistenthashing;

import java.util.ArrayList;
import com.dds.plugin.hashing.MurmurHashFunction;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.utils.Helper;
import com.dds.cluster.Node;

public class ConsistentHashing implements RoutingInterface{

	private final HashingInterface hashFunction;
	private final int numberOfReplicas;
	private final TreeMap<Integer, Node> circle = new TreeMap<Integer, Node>();

	public ConsistentHashing(
			HashingInterface hashFunction, 
			int numberOfReplicas, 
			Collection<Node> nodes) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;

		for (Node node : nodes) {
			addNode(node);
		}
	}

	public void addNode(Node node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash(node.getNodeName().getBytes()), node);
		}
	}

	public void removeNode(Node node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash((node.toString() + i).getBytes()));
		}
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
	
//	public static void main(String[] args)
//	{	
//		MurmurHashFunction mhf = new MurmurHashFunction();
//		Collection<Node> nodes = new ArrayList<Node>();
//		Node n;
//		for(int i=0; i<5;i++)
//		{
//			n = new Node();
//			n.setNodeId(i);
//			n.setNodeName("Node:"+i);
//			nodes.add(n);
//		}
//		
//		
//		ConsistentHashing ch = new ConsistentHashing(mhf, 2, nodes);
//		
//		int hashedKeyOfValue = mhf.hash("Hello".getBytes());
//		n = getNode(hashedKeyOfValue);
//		
//		/*
//		 * When you get the Node object, persist the value in that given node.
//		 * The  Node object will be returned by ConsistentHashing.java
//		 */
//		
//		System.out.println(n.getNodeName());
//	}
}