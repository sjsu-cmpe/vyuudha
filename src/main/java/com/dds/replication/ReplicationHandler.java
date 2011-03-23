package com.dds.replication;

import com.dds.utils.Helper;

/**
 * @author ravid
 *
 */
public class ReplicationHandler {

	public ReplicationHandler() {
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
	
	public Object invoke(String buffer, int writes) {
		return invoke(Helper.getBytes(buffer), writes);
	}
	
	public Object invoke(byte[] buffer, int writes) {
		
		return null;
	}
	
	public Object replicate(byte[] buffer) {
		
		return null;
	}
}
