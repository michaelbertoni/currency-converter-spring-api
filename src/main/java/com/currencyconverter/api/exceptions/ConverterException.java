package com.currencyconverter.api.exceptions;

public class ConverterException extends Exception {

	private static final long serialVersionUID = -5465291729586362554L;
	
	private String errorMessage;
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public ConverterException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public ConverterException() {
		super();
	}
	
}
