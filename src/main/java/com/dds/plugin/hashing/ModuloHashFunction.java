package com.dds.plugin.hashing;

import com.dds.interfaces.hashing.HashingInterface;

public class ModuloHashFunction implements HashingInterface {

	public int hash(byte[] data) {
		return data.hashCode()%data.length;
	}
}
