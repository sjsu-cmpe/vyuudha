/**
 * 
 */
package com.dds.utils;

import com.dds.interfaces.APIInterface;

/**
 * @author ravid
 *
 */
public class PluginMapTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PluginMap<String, Object> keyValue = new PluginMap<String, Object>();
		
		APIInterface api = (APIInterface)keyValue.get("API");
		try {
			api.nativeAPI("arg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
