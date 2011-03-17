package com.dds.utils;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLConfigParser {

	//public void readNodes() {
	public static void main(String[] args)
	{
		try {
			final String pathToFile = "config/nodes.xml";
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(pathToFile));

			// normalize text representation
			doc.getDocumentElement().normalize();

			NodeList listOfNodes = doc.getElementsByTagName("node");
			int nodeCount = listOfNodes.getLength();
			System.out.println("Total no of nodes : " + nodeCount);

			for (int s = 0; s < listOfNodes.getLength(); s++) {

				Node firstNode = listOfNodes.item(s);
				if (firstNode.getNodeType() == Node.ELEMENT_NODE) {

					Element firstNodeElement = (Element) firstNode;
					
					NodeList nodeIdList = firstNodeElement.getElementsByTagName("nodeId");
					Element nodeIdElement = (Element) nodeIdList.item(0);
					NodeList textNodeIdList = nodeIdElement.getChildNodes();
					System.out.println("nodeId : "+ ((Node) textNodeIdList.item(0)).getNodeValue().trim());
					
					NodeList nodeIpAddressList = firstNodeElement.getElementsByTagName("nodeIpAddress");
					Element nodeIpAddressElement = (Element) nodeIpAddressList.item(0);
					NodeList textnodeIpAddressList = nodeIpAddressElement.getChildNodes();
					System.out.println("nodeIpAddress : "+ ((Node) textnodeIpAddressList.item(0)).getNodeValue().trim());
					
					NodeList nodeExternalPortList = firstNodeElement.getElementsByTagName("nodeExternalPort");
					Element nodeExternalPortElement = (Element) nodeExternalPortList.item(0);
					NodeList textnodeExternalPortList = nodeExternalPortElement.getChildNodes();
					System.out.println("nodeExternalPort "+ ((Node) textnodeExternalPortList.item(0)).getNodeValue().trim());
					
					NodeList nodeInternalPortList = firstNodeElement.getElementsByTagName("nodeInternalPort");
					Element nodeInternalPortElement = (Element) nodeInternalPortList.item(0);
					NodeList textnodeInternalPortList = nodeInternalPortElement.getChildNodes();
					System.out.println("nodeInternalPort "+ ((Node) textnodeInternalPortList.item(0)).getNodeValue().trim());
				}
			}

		} catch (Exception err) {
			err.printStackTrace();
		}
	}
}