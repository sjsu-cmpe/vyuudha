/**
 * 
 */
package com.dds.exception;

/**
 * @author ravid
 *
 */

public class StorageException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable t) {
		super(message, t);
	}

	public StorageException(Throwable t) {
		super(t);
	}
	
	public StorageException() {
		super();
	}
}
