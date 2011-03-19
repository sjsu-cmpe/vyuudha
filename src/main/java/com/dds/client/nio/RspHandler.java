package com.dds.client.nio;

import com.dds.utils.Helper;

public class RspHandler {
	private byte[] rsp = null;

	public synchronized boolean handleResponse(byte[] rsp) {
		this.rsp = rsp;
		this.notify();
		return true;
	}

	public synchronized Object waitForResponse() {
		while (this.rsp == null) {
			try {
				this.wait();
	 
			} catch (InterruptedException e) {
			}
		}
		
		return (Helper.getObject(this.rsp));
	}
}
