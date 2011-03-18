package com.dds.plugin.hashing;

import com.dds.interfaces.HashingInterface;

public class HashingContext implements HashingInterface{
	
	private HashingInterface hashObjectState;

	public HashingContext() {
		 setState(new MurmurHashFunction()); //Make it read from config file
	}

	public void setState(HashingInterface newHashObjectState) {
		this.hashObjectState = newHashObjectState;
	}
	
	public int hash(byte[] data) {
		return this.hashObjectState.hash(data);
	}
}
