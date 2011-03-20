package com.dds.core;

import com.dds.interfaces.MembershipInterface;
import com.dds.plugin.membership.gossip.GossipProtocol;

public class MembershipContext implements MembershipInterface {

	private MembershipInterface membershipObjectState;

	public MembershipContext() {
		MembershipInterface ch = new GossipProtocol();
		setState(ch);
	}

	public void setState(MembershipInterface newMembershipObjectState) {
		this.membershipObjectState = newMembershipObjectState;
	}

	@Override
	public void sendNodeList() {
		this.membershipObjectState.sendNodeList();
	}

	@Override
	public void receiveNodeList() {
		this.membershipObjectState.receiveNodeList();
	}

	@Override
	public void getLocalNodeList() {
		this.membershipObjectState.getLocalNodeList();
	}

	@Override
	public void setLocalNodeList() {
		this.membershipObjectState.setLocalNodeList();
	}

}
