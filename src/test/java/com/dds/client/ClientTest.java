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
		long time_start = System.currentTimeMillis();
		try {
			
			for (int i = 0; i < 2; i++) {
				handle.createConnection("nio:127.0.0.1:8001");
				handle.put("key_" + i,"value_" + i);
				System.out.println("Inserted " + i);
				handle.closeConnection();
			}
			
			System.out.println("Done!");
			handle.createConnection("nio:127.0.0.1:8001");
			System.out.println(handle.get("key_1"));
			handle.closeConnection();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		long time_end = System.currentTimeMillis();
		System.out.println(time_end);
		System.out.println("Time Taken : " + (time_end - time_start));
		
	}
}
