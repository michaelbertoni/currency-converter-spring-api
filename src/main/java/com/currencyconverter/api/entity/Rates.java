package com.currencyconverter.api.entity;

import java.util.Map;

public class Rates {
	
	private final String base;
	private final String date;
	private final Map<String, Float> rates;
	
	public Rates(String base, String date, Map<String, Float> rates) {
		super();
		this.base = base;
		this.date = date;
		this.rates = rates;
	}
	
	public String getBase() {
		return base;
	}
	public String getDate() {
		return date;
	}
	public Map<String, Float> getRates() {
		return rates;
	}
	
}
