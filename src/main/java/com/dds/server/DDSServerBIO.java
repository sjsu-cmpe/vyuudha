package com.dds.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.dds.interfaces.ServerInterface;

public class DDSServerBIO implements ServerInterface {
	private ServerSocket server;

	private void handleConnection(ServerSocket server) {
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
			server = new ServerSocket(port, 0, hostAddress);
			DDSServerBIO ddsServerObj = new DDSServerBIO();
			ddsServerObj.handleConnection(server);
		} catch (IOException e) {
			
		}
	}

	public void stop(InetAddress hostAddress, int port) {
		// TODO Auto-generated method stub
	}
}
