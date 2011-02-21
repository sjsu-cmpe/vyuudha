package com.dds.server.nio;

import java.nio.channels.SocketChannel;

class ServerDataEvent {
	public DDSServerNIO server;
	public SocketChannel socket;
	public byte[] data;

	public ServerDataEvent(DDSServerNIO server, SocketChannel socket, byte[] data) {
		this.server = server;
		this.socket = socket;
		this.data = data;
	}
}