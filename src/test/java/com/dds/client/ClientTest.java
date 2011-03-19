/**
 * 
 */
package com.dds.client;

/**
 * @author ravid
 *
 */
public class ClientTest {

	public static void main(String[] args) {
		ClientHandler handle = new ClientHandler();
		
		try {
			handle.createConnection("bio:127.0.0.1:9092");
			System.out.println(handle.get("amazon"));
			
			handle.closeConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
