package com.dds.client.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import com.dds.properties.Property;
import com.dds.utils.Helper;

public class DDSClientBIO {
	public static void main(String[] args) {
        try {
            //
            // Create a connection to the server socket on the server application
            //
        	Map<String, String> props = Property.getProperty().getServerConfigProperties();
            InetAddress host = InetAddress.getByName(props.get("server_ip"));
            int port = Integer.parseInt(props.get("server_port"));
            Socket socket = new Socket(host.getHostName(), port);

            //
            // Send a message to the client application
            //
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("put,serverBIO,test1");

            //
            // Read and display the response message sent by server application
            //
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) Helper.getObject((byte[]) ois.readObject());
            System.out.println("Message: " + message);
            
            
            //
            // Send a message to the client application
            //
            oos.close();
            ois.close();
            socket.close();
            
            socket = new Socket(host.getHostName(), port);
            
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("get,serverBIO");

            //
            // Read and display the response message sent by server application
            //
            ois = new ObjectInputStream(socket.getInputStream());
            message = (String) Helper.getObject((byte[]) ois.readObject());
            System.out.println("Message: " + message);

            ois.close();
            oos.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
