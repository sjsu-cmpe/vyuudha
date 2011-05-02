package com.dds.cluster;

import java.io.Serializable;

import com.dds.plugin.membership.GossipProtocol;


/*
 * The values of the values will be fetched from the config file.
 */
public class Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int nodeId;
	private String nodeIpAddress;
	private int nodeExternalPort;
	private int nodeInternalPort;
	private int nodeReplicationPort;
	private int nodeRoutingPort;
	private int heartbeat;
	private transient TimeoutTimer timeoutTimer;

	public Node(int nodeId, String nodeIpAddress, int nodeExternalPort, 
			int nodeInternalPort, int nodeReplication, int nodeRouting) {
		this.nodeId = nodeId;
		this.nodeIpAddress = nodeIpAddress;
		this.nodeExternalPort = nodeExternalPort;
		this.nodeInternalPort = nodeInternalPort;
		this.nodeReplicationPort = nodeReplication;
		this.nodeRoutingPort = nodeRouting;
		this.heartbeat = 10;
		//2000 is the t_cleanup for gossiping
		this.timeoutTimer = new TimeoutTimer(2000, new GossipProtocol(), this);
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
	
	public void setReplicationPort(Integer nodeReplication) {
		this.nodeReplicationPort = nodeReplication;
	}
	
	public void setRoutingPort(Integer nodeRouting) {
		this.nodeRoutingPort = nodeRouting;
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
	
	public int getReplicationPort() {
		return this.nodeReplicationPort;
	}

	public int getRoutingPort() {
		return this.nodeRoutingPort;
	}
	
	public void startTimeoutTimer() {
		this.timeoutTimer.start();
	}

	public void resetTimeoutTimer() {
		this.timeoutTimer.reset();
	}

	public int getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(int heartbeat) {
		this.heartbeat = heartbeat;
	}

	@Override
	public String toString() {
		return "Member [nodeId =" + nodeId + ", address=" + nodeIpAddress + ", heartbeat=" + heartbeat + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeIpAddress == null) ? 0 : nodeIpAddress.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Node other = (Node) obj;
		if (nodeIpAddress == null) {
			if (other.nodeIpAddress != null) {
				return false;
			}
		} else if (!nodeIpAddress.equals(other.nodeIpAddress)) {
			return false;
		}
		return true;
	}
}