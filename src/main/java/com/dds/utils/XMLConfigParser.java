package com.dds.utils;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public class XMLConfigParser {
	private static ArrayList<com.dds.cluster.Node> nodes = new ArrayList<com.dds.cluster.Node>();
	
	public static ArrayList<com.dds.cluster.Node> readNodes(String pathToFile) {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(pathToFile));

			// normalize text representation
			doc.getDocumentElement().normalize();

			NodeList listOfNodes = doc.getElementsByTagName("node");

			for (int s = 0; s < listOfNodes.getLength(); s++) {
				org.w3c.dom.Node firstNode = listOfNodes.item(s);
				if (firstNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {

					Element firstNodeElement = (Element) firstNode;
					
					NodeList nodeIdList = firstNodeElement.getElementsByTagName("nodeId");
					Element nodeIdElement = (Element) nodeIdList.item(0);
					NodeList textNodeIdList = nodeIdElement.getChildNodes();
					int nodeId = Integer.parseInt(((org.w3c.dom.Node) textNodeIdList.item(0)).getNodeValue().trim());
					
					NodeList nodeIpAddressList = firstNodeElement.getElementsByTagName("nodeIpAddress");
					Element nodeIpAddressElement = (Element) nodeIpAddressList.item(0);
					NodeList textnodeIpAddressList = nodeIpAddressElement.getChildNodes();
					String nodeIpAddress = ((org.w3c.dom.Node) textnodeIpAddressList.item(0)).getNodeValue().trim();
					
					NodeList nodeExternalPortList = firstNodeElement.getElementsByTagName("nodeExternalPort");
					Element nodeExternalPortElement = (Element) nodeExternalPortList.item(0);
					NodeList textnodeExternalPortList = nodeExternalPortElement.getChildNodes();
					Integer nodeExternalPort = Integer.parseInt(((org.w3c.dom.Node) textnodeExternalPortList.item(0)).getNodeValue().trim());
					
					NodeList nodeInternalPortList = firstNodeElement.getElementsByTagName("nodeInternalPort");
					Element nodeInternalPortElement = (Element) nodeInternalPortList.item(0);
					NodeList textnodeInternalPortList = nodeInternalPortElement.getChildNodes();
					Integer nodeInternalPort = Integer.parseInt(((org.w3c.dom.Node) textnodeInternalPortList.item(0)).getNodeValue().trim());

					NodeList nodeReplicationList = firstNodeElement.getElementsByTagName("nodeReplication");
					Element nodeReplicationElement = (Element) nodeReplicationList.item(0);
					NodeList textnodeReplicationList = nodeReplicationElement.getChildNodes();
					Integer nodeReplication = Integer.parseInt(((org.w3c.dom.Node) textnodeReplicationList.item(0)).getNodeValue().trim());

					com.dds.cluster.Node node = new com.dds.cluster.Node(nodeId, nodeIpAddress, nodeExternalPort, nodeInternalPort, nodeReplication);
					
					nodes.add(node);
				}
			}

		} catch (Exception err) {
			err.printStackTrace();
		}
		return nodes;
	}
}