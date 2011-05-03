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
		
		APIInterface api = (APIInterface)keyValue.get(PluginEnum.API.getValue());
		try {
			api.createConnection();
			api.put("key_1", "value_1");
			System.out.println(api.get("key_1"));
			api.closeConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
