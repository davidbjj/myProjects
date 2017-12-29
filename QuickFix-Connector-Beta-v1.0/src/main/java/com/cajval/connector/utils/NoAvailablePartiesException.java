package com.cajval.connector.utils;

public class NoAvailablePartiesException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoAvailablePartiesException() {
	}

	public NoAvailablePartiesException(Throwable cause) {
		super(cause);
	}

	public NoAvailablePartiesException(String message) {
		super(message);
	}

	public NoAvailablePartiesException(String message, Throwable cause) {
		super(message, cause);
	}

}
