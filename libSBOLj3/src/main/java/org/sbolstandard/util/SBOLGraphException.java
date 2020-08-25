package org.sbolstandard.util;

public class SBOLGraphException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -523239991112483227L;

	public SBOLGraphException(String message) {
		super(message);
	}
	
	public SBOLGraphException(String message, Exception e) {
		super(message,e);
	}

}
