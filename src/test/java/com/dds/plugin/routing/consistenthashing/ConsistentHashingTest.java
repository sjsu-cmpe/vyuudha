package com.dds.plugin.routing.consistenthashing;

import java.util.ArrayList;
import java.util.Collection;

import com.dds.cluster.Node;
import com.dds.core.HashingContext;
import com.dds.plugin.routing.ConsistentHashing;

public class ConsistentHashingTest {
	public static void main(String[] args) {
		HashingContext mhf = new HashingContext();
		Collection<Node> nodes = new ArrayList<Node>();
		Node n;
		for (int i = 0; i < 5; i++) {
			n = new Node(i, null, i, i);
			n.setNodeId(i);
			nodes.add(n);
		}

		ConsistentHashing ch = new ConsistentHashing();

		int hashedKeyOfValue = mhf.hash("Hello".getBytes());
		n = ch.getNode(hashedKeyOfValue);

		/*
		 * When you get the Node object, persist the value in that given node.
		 * The Node object will be returned by ConsistentHashing.java
		 */
	}
}
