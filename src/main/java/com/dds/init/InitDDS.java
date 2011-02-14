package com.dds.init;

import java.io.IOException;

import com.dds.server.nio.DDSServer;
import com.dds.server.nio.DDSWorker;

public class InitDDS {
	
	public static void main(String[] args) {
		try {
			DDSWorker worker = new DDSWorker();
			new Thread(worker).start();
			new Thread(new DDSServer(null, 9090, worker)).start();
			System.out.println("Vyuudha Server Started...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
