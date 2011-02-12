/**
 * 
 */
package com.dds.storage;

import java.lang.reflect.InvocationTargetException;
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
		//String argument = "put,keyUtkarsh,KuchBhi";
		String argument = "get,keyUtkarsh1";
		try {
			Object retObj = dbRoot.invoke(Helper.getBytes(argument));
			if (retObj != null) {
				String retVal = (String) retObj;
				System.out.println("Value Returned :" + retVal);
			}
			else {
				System.out.println("Key not found!");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO LOG!
			System.out.println("Could not fetch : " + e.getMessage());
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println("Key not found : " + e.getMessage());
		}

	}

}
