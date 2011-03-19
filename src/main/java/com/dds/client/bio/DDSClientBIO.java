package com.dds.client.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.dds.client.interfaces.DDSClientInterface;
import com.dds.exception.UnsupportedException;
import com.dds.utils.Helper;

public class DDSClientBIO implements DDSClientInterface {
	
	Logger logger = Logger.getLogger(DDSClientBIO.class);
	
	private InetAddress host;
	private int port;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	/**
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	public Object write(String stream) throws IOException, ClassNotFoundException {
		if (socket == null || oos == null) {
			initialize();
		}
		oos.writeObject(stream);
		ois = new ObjectInputStream(socket.getInputStream());
        Object message = Helper.getObject((byte[]) ois.readObject());
        
        logger.info("write");
        exit();
        return message;
	}

	/**
	 *
	 * @throws IOException
	 */
	public void exit() throws IOException {
		if (oos != null || ois != null || socket !=null) {
			oos.close();
			ois.close();
			socket.close();
			
			oos = null;
			ois = null;
			socket = null;
		}		
		logger.info("exit");
	}

	/**
	 * @throws UnknownHostException
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws UnsupportedException 
	 */
	public void initialize(String[] serverInfo) throws UnknownHostException,
			NumberFormatException, IOException, UnsupportedException {
		if (serverInfo.length != 2) {
			throw new UnsupportedException("Insufficient parameters");
		}
		host = InetAddress.getByName(serverInfo[0]);
		port = Integer.parseInt(serverInfo[1]);
		initialize();

		logger.info("initialized");
	}
	
	private void initialize() throws UnknownHostException, IOException  {
		if (socket == null || oos == null) {
			socket = new Socket(host.getHostName(), port);
			oos = new ObjectOutputStream(socket.getOutputStream());
		}	
	}
}
