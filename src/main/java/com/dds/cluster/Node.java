package com.dds.cluster;

import java.io.Serializable;




/*
 * The note attributed go in here. The values of the values will be fetched from the config file.
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
	private int heartbeat;
	private transient TimeoutTimer timeoutTimer;

	public Node(int nodeId, String nodeIpAddress, int nodeExternalPort, int nodeInternalPort) {
		this.nodeId = nodeId;
		this.nodeIpAddress = nodeIpAddress;
		this.nodeExternalPort = nodeExternalPort;
		this.nodeInternalPort = nodeInternalPort;
		this.heartbeat = 10;
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
		return "Member [address=" + nodeIpAddress + ", heartbeat=" + heartbeat + "]";
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