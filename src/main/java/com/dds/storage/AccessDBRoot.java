/**
 * 
 */
package com.dds.storage;

import java.net.UnknownHostException;

import com.dds.utils.Helper;

/**
 * @author ravid
 *
 */
public class AccessDBRoot {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBRoot dbRoot = new DBRoot();
		String argument = "getVersion,arg1,arg2,arg3";
		try {
			Object retObj = dbRoot.invoke(Helper.getBytes(argument));
			System.out.println(retObj.getClass());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO LOG!
			System.out.println("Could not fetch : " + e.getMessage());
		}

	}

}
