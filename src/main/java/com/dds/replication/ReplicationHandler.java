package com.dds.replication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.dds.exception.UnsupportedException;
import com.dds.interfaces.APIInterface;
import com.dds.interfaces.ServerInterface;
import com.dds.utils.Property;

/**
 * @author ravid
 *
 */
public class ReplicationHandler implements APIInterface {

	private static Map<String, String> replicationMap;
	private InetAddress replicationAddress;
	private ServerInterface replicationIO;
	private int replicationPort ;
	
	private String nextNodeAddress;
	private String nextNodePort;
	
	public void init() throws UnknownHostException {
		init(null);
	}
	
	public void init(String folder) throws UnknownHostException {
		
		replicationMap = Property.getProperty().getReplicationProperties(folder);
		//Setup replication server and start listening to requests
		replicationAddress = InetAddress.getByName(replicationMap.get("server_ip"));
		replicationIO = new ReplicationServerNIO();
		replicationPort = Integer.parseInt(replicationMap.get("server_port_internal"));
		replicationIO.start(replicationAddress, replicationPort);
		
		ReplicationServerNIO replication = new ReplicationServerNIO();
		replication.start(replicationAddress, replicationPort);
	}
	
	public void setNextNodeInfo(String address, String port) {
		nextNodeAddress = address;
		nextNodePort = port;
		
		nextNodeAddress = replicationMap.get("server_ip");
		nextNodePort = replicationMap.get("server_port_internal");
	}
	
	public void replicate(String key, String value, int factor) throws Exception {
		ReplicationClientHandler clientHandler = new ReplicationClientHandler();

		clientHandler.createConnection(nextNodeAddress + ":" + nextNodePort);
		clientHandler.replicate(key, value, factor);
	}

	@Override
	public void createConnection() throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public void createConnection(String bootstrapUrl) throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public String get(String key) throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public void put(String key, String value) throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public void delete(String key) throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public void closeConnection() throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public Boolean contains(String key) throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

	@Override
	public Object nativeAPI(String... args) throws Exception {
		throw new UnsupportedException("Unsupported Method");
	}

}
