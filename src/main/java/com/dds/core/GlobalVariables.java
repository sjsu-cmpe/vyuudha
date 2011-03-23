package com.dds.core;

import java.util.Collection;

import com.dds.cluster.Node;
import com.dds.interfaces.APIInterface;
import com.dds.utils.PluginMap;

public enum GlobalVariables {
	INSTANCE;
	
	public Collection<Node> nodes;
	public HashingContext hash;
	public MembershipContext membership;
	public ServerContext server;
	public APIInterface storage;
	
	public PluginMap<String, Object> map = new PluginMap<String, Object>();
}
