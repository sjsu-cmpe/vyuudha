package com.dds.plugin.routing.consistenthashing;

import java.util.ArrayList;
import java.util.Collection;

import com.dds.cluster.Node;
import com.dds.core.GlobalVariables;
import com.dds.interfaces.HashingInterface;
import com.dds.plugin.routing.ConsistentHashing;

public class ConsistentHashingTest {
	public static void main(String[] args) {
		//HashingContext mhf = new HashingContext();
		HashingInterface mhf = GlobalVariables.INSTANCE.getHash();
		Collection<Node> nodes = new ArrayList<Node>();
		Node n;
		for (int i = 1; i < 5; i++) {
			n = new Node(i, "ip" + i, i, i, i);
			nodes.add(n);
		}

		ConsistentHashing ch = new ConsistentHashing();
		ch.setHashingTechnique(mhf);
		ch.setupRoutingCluster(nodes);
		
		Node n1 = ch.getNextNode();
		System.out.println(n1.toString());
		System.out.println(n1.getNodeIpAddress());
		System.out.println(n1.getReplicationPort());
		
//		int hashedKeyOfValue = mhf.hash("Hello".getBytes());
//		n = ch.getNode(hashedKeyOfValue);

		/*
		 * When you get the Node object, persist the value in that given node.
		 * The Node object will be returned by ConsistentHashing.java
		 */
	}
}
