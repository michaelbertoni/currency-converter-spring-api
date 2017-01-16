package com.currencyconverter.api.exception;

public class FixerIoServerException extends Exception {

	private static final long serialVersionUID = 6525060088565203524L;
	
	public FixerIoServerException(Exception e) {
		super(e);
	}
	
	public FixerIoServerException() {
	}
	
}
