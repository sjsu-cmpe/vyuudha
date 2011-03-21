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
			handle.createConnection("nio:127.0.0.1:9092");
			handle.put("ebay","san jose");
			System.out.println(handle.get("amazon"));
			
			handle.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
