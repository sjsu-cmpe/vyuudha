/**
 * 
 */
package com.dds.exception;

/**
 * @author ravid
 *
 */

public class UnsupportedException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedException(String message) {
		super(message);
	}

	public UnsupportedException(String message, Throwable t) {
		super(message, t);
	}

	public UnsupportedException(Throwable t) {
		super(t);
	}
	
	public UnsupportedException() {
		super();
	}
}
