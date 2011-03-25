package com.dds.interfaces;

import javax.management.Notification;

import com.dds.cluster.Node;

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
	public void sendMembershipList();
	/**
	 * This should get the member(s) to notify
	 */
	public Node getMemberToNotify();
	
	/**
	 * A listener, which will keep listening to new membership messages
	 */
	public void handleNotification(Notification notification, Object handback);
}
