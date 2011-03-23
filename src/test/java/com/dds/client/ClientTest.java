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
			handle.put("ebay","san jose3");
			System.out.println(handle.get("ebay"));
			
			handle.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
