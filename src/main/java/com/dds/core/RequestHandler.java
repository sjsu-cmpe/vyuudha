package com.dds.core;

import org.apache.log4j.Logger;

import com.dds.client.ClientHandler;
import com.dds.cluster.Node;
import com.dds.interfaces.RoutingInterface;
import com.dds.utils.Helper;


/**
 * @author utkarsh
 *
 * This will receive request from NIO/BIO
 */
public class RequestHandler {

	Logger logger = Logger.getLogger(RequestHandler.class);

	private RoutingInterface routing = GlobalVariables.INSTANCE.getRouting();
	private boolean singleInstance = GlobalVariables.INSTANCE.isSingleInstance();
	private int nodeId = GlobalVariables.INSTANCE.getNodeId();
	/**
	 * Makes a call to the DBRoot, Vyuudha storage abstraction engine
	 * 
	 * @param dataCopy
	 * @return object from the core storage layer
	 */
	public Object storageCall(byte[] dataCopy) {
		return storageCall(dataCopy, false);
	}
	
	public Object storageCall(byte[] dataCopy, boolean persist) {
		String buf = (String) Helper.getObject(dataCopy);
		String[] bufArray = buf.split(",");
		String key = bufArray[1].trim();

		Node node = routing.getNode(key);

		if (node.getNodeId() == nodeId || persist ||
				singleInstance) {
			return persistData(dataCopy);
		} else {
			System.out.println("Request routed to " + node);
			try {
				String bootstrapUrl  = "nio:" +  node.getNodeIpAddress() + ":" + node.getRoutingPort();
				ClientHandler clientHandle = new ClientHandler();
				clientHandle.createConnection(bootstrapUrl);
				return clientHandle.sendMessage(dataCopy);
			} catch (Exception e) {
				logger.info(e.getLocalizedMessage());
			}
		}
		return "Error";
	}

	private Object persistData(byte[] dataCopy) {
		System.out.println("Persisting");
		StorageHandler dbRoot = new StorageHandler();
		Object objectReturned = null;
		try {
			objectReturned = dbRoot.invoke(dataCopy);
			if (objectReturned != null) {
				return objectReturned;
			} else {
				return "Error";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objectReturned;

	}

}
