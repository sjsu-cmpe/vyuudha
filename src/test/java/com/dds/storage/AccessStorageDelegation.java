/**
 * 
 */
package com.dds.storage;


import com.dds.core.StorageHandler;
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
		try {

			StorageHandler delegate = new StorageHandler();
			String argument = "get,hello,world";
			byte[] buffer = Helper.getBytes(argument);
			System.out.println(delegate.invoke(buffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
