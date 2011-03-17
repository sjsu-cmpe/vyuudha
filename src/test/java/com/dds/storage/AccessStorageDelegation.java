/**
 * 
 */
package com.dds.storage;


import com.dds.utils.Helper;

/**
 * @author ravid
 *
 */
public class AccessStorageDelegation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StorageHandler delegate = new StorageHandler();
		String argument = "get,hello,world";
		byte[] buffer = Helper.getBytes(argument);
		try {
			System.out.println(delegate.invoke(buffer));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
