package com.dds.core;

import java.net.InetAddress;

import com.dds.interfaces.ServerInterface;
import com.dds.server.nio.DDSServerNIO;

public class ServerContext implements ServerInterface{
	private ServerInterface serverObjectState;
	
	public ServerContext(){
		ServerInterface server = new DDSServerNIO();
		setState(server);
	}
	
	public void setState(ServerInterface newServerObjectState) {
		this.serverObjectState = newServerObjectState;
	}
	
	@Override
	public void start(InetAddress hostAddress, int port) {
		this.serverObjectState.start(hostAddress, port);
	}
	
	@Override
	public void stop(InetAddress hostAddress, int port) {
		this.serverObjectState.stop(hostAddress, port);
	}
}