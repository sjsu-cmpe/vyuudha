package com.dds.core;

import java.util.ArrayList;
import java.util.Map;

import com.dds.cluster.Node;
import com.dds.interfaces.APIInterface;
import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.MembershipInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.interfaces.ServerInterface;
import com.dds.properties.Property;
import com.dds.utils.PluginEnum;
import com.dds.utils.PluginMap;

public enum GlobalVariables {
	INSTANCE;
	
	public ArrayList<Node> nodeList;
	public ArrayList<Node> deadNodeList = new ArrayList<Node>();
	
	/**
	 * server.properties data
	 */
	Map<String, String> props = Property.getProperty().getServerConfigProperties();
	public int nodeId = Integer.parseInt(props.get("node_id"));
	public String serverType = props.get("server_type");
	public int serverPortExternal = Integer.parseInt(props.get("server_port_external"));
	public int serverPortInternal = Integer.parseInt(props.get("server_port_internal"));
	public String serverIp = props.get("server_ip");
	
	private PluginMap<String, Object> map = new PluginMap<String, Object>();
	
	public APIInterface getAPI() {
		return (APIInterface)map.get(PluginEnum.API.toString());
	}
	
	public ServerInterface getServer() {
		return (ServerInterface)map.get(PluginEnum.SERVER.toString());
	}
	
	public HashingInterface getHash() {
		return (HashingInterface)map.get(PluginEnum.HASHING.toString());
	}
	
	public MembershipInterface getMembership() {
		return (MembershipInterface)map.get(PluginEnum.MEMBERSHIP.toString());
	}
	
	public RoutingInterface getRouting() {
		return (RoutingInterface)map.get(PluginEnum.ROUTING.toString());
	}
}
