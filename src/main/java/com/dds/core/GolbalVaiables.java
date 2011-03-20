package com.dds.core;

import java.util.Collection;

import com.dds.cluster.Node;

public enum GolbalVaiables {
	GLOBAL,
	HASHING,
	ROUTING,
	MEMBERSHIP,
	STORAGE;
	
	public Collection<Node> nodes;
}
