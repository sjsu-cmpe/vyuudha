package com.dds.replication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dds.exception.UnsupportedException;
import com.dds.interfaces.APIInterface;
import com.dds.interfaces.ServerInterface;
import com.dds.utils.Property;

/**
 * @author ravid
 *
 */
public class ReplicationHandler implements APIInterface {

	Logger logger = Logger.getLogger(ReplicationHandler.class);
	
	private Map<String, String> replicationMap;
	private InetAddress replicationAddress;
	private ServerInterface replicationIO;
	private int replicationPort ;
	
	private String nextNodeAddress;
	private String nextNodePort;

	private APIInterface dbInterface;
	
	public ReplicationHandler() {
		this(null);
	}
	
	public ReplicationHandler(String folder) {
		replicationMap = Property.getProperty().getReplicationProperties(folder);
	}
	
	public void initReplicationServer() throws UnknownHostException {
		initReplicationServer(null);
	}
	
	public void initReplicationServer(String folder) throws UnknownHostException {
		
		replicationMap = Property.getProperty().getReplicationProperties(folder);

		//Setup replication server and start listening to requests
		replicationAddress = InetAddress.getByName(replicationMap.get("server_ip"));
		replicationIO = new ReplicationServerNIO();
		replicationPort = Integer.parseInt(replicationMap.get("server_port_internal"));
		replicationIO.start(replicationAddress, replicationPort);
		
		ReplicationServerNIO replication = new ReplicationServerNIO();
		replication.start(replicationAddress, replicationPort);
		
		System.out.println("Replication Server started at " + replicationAddress.toString() + " : " + replicationPort);
	}
	
	public void setNextNodeInfo(String address, String port) {
		nextNodeAddress = address;
		nextNodePort = port;
	}
	
	public void replicate(String...keyValue) throws Exception {
		
		int factor;
		factor = Integer.parseInt(keyValue[2] == null ? replicationMap.get("writes") : keyValue[2]);
		
		if (keyValue.length < 2 || keyValue.length > 3) {
			logger.error("Insufficient parameters");
			throw new UnsupportedException("Insufficient parameters");
		}
		factor = factor - 1;
		if (factor >= 0) {
			replicate(keyValue[0], keyValue[1], factor);
		}
	}
	
	public void replicate(String key, String value, int factor) throws Exception {
		if ((factor + 1) != Integer.parseInt(replicationMap.get("writes"))) {
			dbInterface.replicate(key, value);
		}
		
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

	@Override
	public void replicate(String key, String value) throws Exception {
		replicate(new String[]{key, value, null});
	}

	public void setDBObject(APIInterface dbInterface) {
		this.dbInterface = dbInterface;
	}

}
