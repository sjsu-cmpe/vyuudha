package com.dds.server.nio;

import java.nio.channels.SocketChannel;

class ServerDataEvent {
	public DDSServer server;
	public SocketChannel socket;
	public byte[] data;

	public ServerDataEvent(DDSServer server, SocketChannel socket, byte[] data) {
		this.server = server;
		this.socket = socket;
		this.data = data;
	}
}