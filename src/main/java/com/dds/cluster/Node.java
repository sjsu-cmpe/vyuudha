package com.dds.cluster;

/*
 * TO-DO: Need to specify the attributes of a Node.
 */
public class Node {
	private Integer nodeId;
	private String nodeName;
	
	public void setNodeId(Integer nodeId)
	{
		this.nodeId = nodeId;
	}
	
	public Integer getNodeId()
	{
		return this.nodeId;
	}
	
	public void setNodeName(String nodeName)
	{
		this.nodeName = nodeName;
	}
	
	public String getNodeName()
	{
		return this.nodeName;
	}
}