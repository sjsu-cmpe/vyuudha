package com.dds.core;

import java.util.ArrayList;
import java.util.Map;

import com.dds.cluster.Node;
import com.dds.interfaces.APIInterface;
import com.dds.interfaces.HashingInterface;
import com.dds.interfaces.MembershipInterface;
import com.dds.interfaces.RoutingInterface;
import com.dds.interfaces.ServerInterface;
import com.dds.utils.Property;
import com.dds.utils.PluginEnum;
import com.dds.utils.PluginMap;

public enum GlobalVariables {
	INSTANCE;
	
	public ArrayList<Node> nodeList;
	public ArrayList<Node> deadNodeList = new ArrayList<Node>();
	
	/**
	 * server.properties data
	 */
	private Map<String, String> props;
	private int nodeId;
	private String serverType;
	private int serverPortExternal;
	private int serverPortInternal;
	private String serverIp;
	
	private PluginMap<String, Object> map = new PluginMap<String, Object>();
	
	/**
	 * @return the nodeId
	 */
	public int getNodeId() {
		if (props == null || props.isEmpty()) {
			setProperties();
		}
		return nodeId;
	}

	/**
	 * @return the serverType
	 */
	public String getServerType() {
		if (props == null || props.isEmpty()) {
			setProperties();
		}
		return serverType;
	}

	/**
	 * @return the serverPortExternal
	 */
	public int getServerPortExternal() {
		if (props == null || props.isEmpty()) {
			setProperties();
		}
		return serverPortExternal;
	}

	/**
	 * @return the serverPortInternal
	 */
	public int getServerPortInternal() {
		if (props == null || props.isEmpty()) {
			setProperties();
		}
		return serverPortInternal;
	}

	/**
	 * @return the serverIp
	 */
	public String getServerIp() {
		if (props == null || props.isEmpty()) {
			setProperties();
		}
		return serverIp;
	}

	/**
	 * If we intend to pick properties from a different folder
	 * (which is also in the project root folder)  then we first
	 * setProperties and then make the usual calls
	 * @param folderName
	 */
	public void setProperties(String folderName) {
		if (folderName != null) {
			props = Property.getProperty().getServerConfigProperties(folderName);
		} else {
			props = Property.getProperty().getServerConfigProperties();
		}
		nodeId = Integer.parseInt(props.get("node_id"));
		serverType = props.get("server_type");
		serverPortExternal = Integer.parseInt(props.get("server_port_external"));
		serverPortInternal = Integer.parseInt(props.get("server_port_internal"));
		serverIp = props.get("server_ip");
	}
	
	/**
	 * setProperties by default picks up properties files from the default
	 * config folder from project root. User need not specify it explicitly
	 */
	public void setProperties() {
		setProperties(null);
	}
		
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
