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
			handle.createConnection("nio:127.0.0.1:8011");
			handle.put("ebay1","san jose4");
			System.out.println(handle.get("ebay1"));
			
			handle.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
