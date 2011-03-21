package com.dds.core;

import java.util.Collection;

import com.dds.cluster.Node;

public enum GlobalVariables {
	INSTANCE;
	
	public Collection<Node> nodes;
	public HashingContext hash;
	public MembershipContext membership;
	public ServerContext server;
}
