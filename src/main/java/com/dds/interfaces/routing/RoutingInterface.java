package com.dds.interfaces.routing;

import com.dds.cluster.Node;

public interface RoutingInterface {
	public void addNode(Node node);
	public void removeNode(Node node);
	public Node getNode(Object key);
}
