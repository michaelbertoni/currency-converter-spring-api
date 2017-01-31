package com.currencyconverter.api.entity;

/**
 * Entité renvoyée en cas d'erreur lors de l'appel d'une API
 * @author michael
 *
 */
public class ErrorResponse {
	private int errorCode;
	private String message;
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}