package com.dds.core;

import java.util.ArrayList;
import java.util.List;
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

	public List<Node> nodeList;
	public List<Node> deadNodeList = new ArrayList<Node>();

	/**
	 * server.properties data
	 */
	private Map<String, String> props;
	private int nodeId;
	private boolean singleInstance;
	private String serverType;
	private int serverPortExternal;
	private int serverPortInternal;
	private int routingPort;
	private String serverIp;
	private Node currentNode;

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
		singleInstance = Boolean.parseBoolean(props.get("single_instance"));
		serverType = props.get("server_type");
		serverPortExternal = Integer.parseInt(props.get("server_port_external"));
		serverPortInternal = Integer.parseInt(props.get("server_port_internal"));
		routingPort = Integer.parseInt(props.get("routing_port"));
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

	public int getRoutingPort() {
		if (props == null || props.isEmpty()) {
			setProperties();
		}
		return routingPort;
	}

	public Node getLiveNode(int nodeId){
		
		for(int i=0; i<GlobalVariables.INSTANCE.nodeList.size(); i++){
			if(GlobalVariables.INSTANCE.nodeList.get(i).getNodeId().equals(nodeId)){
				return GlobalVariables.INSTANCE.nodeList.get(i);
			}
		}
		
		return null;
	}
	
	public Node getDeadNode(int nodeId){
		
		for(int i=0; i<GlobalVariables.INSTANCE.deadNodeList.size(); i++){
			if(GlobalVariables.INSTANCE.deadNodeList.get(i).getNodeId().equals(nodeId)){
				return GlobalVariables.INSTANCE.deadNodeList.get(i);
			}
		}
		
		return null;
	}
	
	public boolean containsLiveNode(int nodeId){
		
		for(int i=0; i<GlobalVariables.INSTANCE.nodeList.size(); i++){
			if(GlobalVariables.INSTANCE.nodeList.get(i).getNodeId().equals(nodeId)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean containsDeadNode(int nodeId){
		
		for(int i=0; i<GlobalVariables.INSTANCE.deadNodeList.size(); i++){
			if(GlobalVariables.INSTANCE.deadNodeList.get(i).getNodeId().equals(nodeId)){
				return true;
			}
		}
		
		return false;
	}
	
	public void removeDeadList(int nodeId){
		for(int i=0; i<GlobalVariables.INSTANCE.deadNodeList.size(); i++){
			if(GlobalVariables.INSTANCE.deadNodeList.get(i).getNodeId().equals(nodeId)){
				GlobalVariables.INSTANCE.deadNodeList.remove(i);
			}
		}
	}
	
	public void removeLiveList(int nodeId){
		for(int i=0; i<GlobalVariables.INSTANCE.nodeList.size(); i++){
			if(GlobalVariables.INSTANCE.nodeList.get(i).getNodeId().equals(nodeId)){
				GlobalVariables.INSTANCE.nodeList.remove(i);
			}
		}
	}
	
	public Node getCurrentNode() {
		if (currentNode == null) {
			for (Node n : nodeList) {
				if (n.getNodeId() == nodeId) {
					currentNode = n;
					break;
				}
			}
		}
		return currentNode;
	}

	/**
	 * @return the singleInstance
	 */
	public boolean isSingleInstance() {
		if (props == null || props.isEmpty()) {
			setProperties();
		}
		return singleInstance;
	}
}
