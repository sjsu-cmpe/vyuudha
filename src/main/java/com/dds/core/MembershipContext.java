package com.dds.core;

import com.dds.interfaces.MembershipInterface;
import com.dds.plugin.membership.AtomicBroadcast;

public class MembershipContext implements MembershipInterface {

	private MembershipInterface membershipObjectState;

	public MembershipContext() {
		MembershipInterface ch = new AtomicBroadcast();
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

	@Override
	public void start() {
		this.membershipObjectState.start();
	}

}
