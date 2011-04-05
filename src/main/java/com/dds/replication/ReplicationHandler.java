package com.dds.replication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.dds.interfaces.ServerInterface;
import com.dds.utils.Helper;
import com.dds.utils.Property;

/**
 * @author ravid
 *
 */
public class ReplicationHandler {

	private Map<String, String> replicationMap = Property.getProperty().getReplicationProperties();
	private InetAddress replicationAddress;
	private ServerInterface replicationIO;
	private int replicationPort ;
	
	public void init() throws UnknownHostException {
		
		//Setup replication server and start listening to requests
		replicationAddress = InetAddress.getByName(replicationMap.get("server_ip"));
		replicationIO = new ReplicationServerNIO();
		replicationPort = Integer.parseInt(replicationMap.get("server_port_internal"));
		replicationIO.start(replicationAddress, replicationPort);
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
