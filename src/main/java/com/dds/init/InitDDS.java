package com.dds.init;

import com.dds.cluster.ClusterCommunication;
import com.dds.server.nio.DDSServerNIO;

public class InitDDS {

	public static void main(String[] args) {
		
		DDSServerNIO ddsNIO = new DDSServerNIO();
		ddsNIO.start(null, 9090);
		System.out.println("Vyuudha NIO Server Started...");
		
		// Starting JGroups channel
		new ClusterCommunication().startCommunication();
		System.out.println("JGroups channel established...");
	}
}