/**
 * 
 */
package com.dds.exception;

import org.apache.log4j.Logger;

/**
 * @author ravid
 *
 */
public class HandleException {

	Logger logger = Logger.getLogger(HandleException.class);
	
	public void handleException(String message, String className) {
		logger.info("In " + className + ":" + message);
	}
}
