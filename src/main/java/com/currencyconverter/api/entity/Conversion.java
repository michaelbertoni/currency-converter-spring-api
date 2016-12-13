package com.currencyconverter.api.entity;

public class Conversion {
	
	private final String currencyFrom;
	private final String currencyTo;
	private final Float valueFrom;
	private final Float valueTo;
	
	public Conversion(String currencyFrom, String currencyTo, Float valueFrom, Float valueTo) {
		super();
		this.currencyFrom = currencyFrom;
		this.currencyTo = currencyTo;
		this.valueFrom = valueFrom;
		this.valueTo = valueTo;
	}

	public String getCurrencyFrom() {
		return currencyFrom;
	}

	public String getCurrencyTo() {
		return currencyTo;
	}

	public Float getValueFrom() {
		return valueFrom;
	}

	public Float getValueTo() {
		return valueTo;
	}
	
}
