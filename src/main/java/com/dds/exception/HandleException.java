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

	static Logger logger = Logger.getLogger(HandleException.class);
	
	public static void handler(String message, String className, String methodName) {
		logger.info("In " + className + "; Method : " + methodName + "; Message : " + message);
	}
}
