package com.dds.interfaces;

/*
 * Used by InitDDS.java to start the server. The hostAddress and port number will
 * be configurable, picked from a properties file, passed by InitDDS.java
 * 
 * Should be implemented by any server: NIO and BIO for now.
 */

import java.net.InetAddress;

public interface ServerInterface {
	public void start(InetAddress hostAddress, int port);
	public void stop(InetAddress hostAddress, int port);
}
