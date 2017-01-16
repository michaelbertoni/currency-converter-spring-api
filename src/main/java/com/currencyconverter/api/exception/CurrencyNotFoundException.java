package com.currencyconverter.api.exception;

public class CurrencyNotFoundException extends Exception {

	private static final long serialVersionUID = 6062352602643148353L;
	
	public CurrencyNotFoundException() {
		super("Une devise au moins est inconnue.");
	}
	
}
