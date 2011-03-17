package com.dds.cluster;

/*
 * The note attributed go in here. The values of the values will be fetched from the config file.
 */
public class Node {
	private int nodeId;
	private String nodeIpAddress;
	private int nodeExternalPort;
	private int nodeInternalPort;

	public Node(int nodeId, String nodeIpAddress, int nodeExternalPort, int nodeInternalPort) {
		this.nodeId = nodeId;
		this.nodeIpAddress = nodeIpAddress;
		this.nodeExternalPort = nodeExternalPort;
		this.nodeInternalPort = nodeInternalPort;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public void setExternalPort(Integer nodeExternalPort) {
		this.nodeExternalPort = nodeExternalPort;
	}

	public void setInternalPort(Integer nodeInternalPort) {
		this.nodeInternalPort = nodeInternalPort;
	}

	public void setNodeIpAddress(String nodeName) {
		this.nodeIpAddress = nodeName;
	}

	public Integer getNodeId() {
		return this.nodeId;
	}

	public String getNodeIpAddress() {
		return this.nodeIpAddress;
	}

	public int getExternalPort() {
		return this.nodeExternalPort;
	}

	public int getInternalPort() {
		return this.nodeInternalPort;
	}
}