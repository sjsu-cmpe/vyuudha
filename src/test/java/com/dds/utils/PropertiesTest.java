/**
 * 
 */
package com.dds.utils;

import com.dds.core.GlobalVariables;

/**
 * @author ravid
 *
 */
public class PropertiesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int id = GlobalVariables.INSTANCE.getNodeId();
		
		System.out.println(id);
		GlobalVariables.INSTANCE.setProperties("config_2");
		id = GlobalVariables.INSTANCE.getNodeId();
		System.out.println(id);
		
		GlobalVariables.INSTANCE.setProperties();
		id = GlobalVariables.INSTANCE.getNodeId();
		System.out.println(id);

	}

}
