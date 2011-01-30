package com.dds.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {

	@SuppressWarnings("unused")
	private void readServerConfig() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("server-config.properties"));
		} catch (IOException e) {
			
		}
	}
}
