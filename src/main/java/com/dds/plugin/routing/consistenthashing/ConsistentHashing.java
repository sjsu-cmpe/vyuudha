package com.dds.plugin.routing.consistenthashing;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import com.dds.interfaces.hashing.HashingInterface;
import com.dds.utils.Helper;
import com.dds.cluster.Node;

public class ConsistentHashing {

	private final HashingInterface hashFunction;
	private final int numberOfReplicas;
	private final SortedMap<Integer, Node> circle = new TreeMap<Integer, Node>();

	public ConsistentHashing(
			HashingInterface hashFunction, 
			int numberOfReplicas, 
			Collection<Node> nodes) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;

		for (Node node : nodes) {
			add(node);
		}
	}

	public void add(Node node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash((node.toString() + i).getBytes()), node);
		}
	}

	public void remove(Node node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash((node.toString() + i).getBytes()));
		}
	}

	public Node get(Object key) {
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