/**
 * 
 */
package com.dds.utils;

/**
 * @author ravid
 *
 */
public enum PluginEnum {
	API ("API"), 
	HASHING ("HASHING"),
	MEMBERSHIP ("MEMBERSHIP"),
	ROUTING ("ROUTING"),
	SERVER ("SERVER");
	
	String value;
	private PluginEnum(String str) {
		this.value = str;
	}
	
	public String getValue() {
		return this.value;
	}
}
