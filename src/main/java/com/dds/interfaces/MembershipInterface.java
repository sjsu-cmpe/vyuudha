package com.dds.interfaces;

/**
 * @author utkarsh
 *
 */
public interface MembershipInterface {
	/**
	 * Start the thread which will keep communicating till eternity.
	 */
	public void start();
	/**
	 * Send the Node list to other Nodes (can be multicast, gossip etc)
	 */
	public void sendNodeList();
	/**
	 * A listener, which will keep listening to new membership messages
	 */
	public void receiveNodeList();
	/**
	 * Get the Collection of Nodes on the local instance.
	 */
	public void getLocalNodeList();
	/**
	 * Update the Collection of Nodes on the local instance, when a Node is added/removed
	 */
	public void setLocalNodeList();
}
