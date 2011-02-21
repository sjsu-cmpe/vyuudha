package com.dds.server.bio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.dds.interfaces.server.ServerInterface;

public class DDSServerBIO implements ServerInterface {
	private ServerSocket server;

	private void handleConnection() {
		System.out.println("Waiting for client message...");

		while (true) {
			try {
				Socket socket = server.accept();
				new ConnectionHandler(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void start(InetAddress hostAddress, int port) {
		try {
			server = new ServerSocket(port);
			DDSServerBIO ddsServerObj = new DDSServerBIO();
			ddsServerObj.handleConnection();
		} catch (IOException e) {
			
		}
	}

	public void stop(InetAddress hostAddress, int port) {
		// TODO Auto-generated method stub
	}
}
