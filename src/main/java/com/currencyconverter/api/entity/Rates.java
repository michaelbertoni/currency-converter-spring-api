package com.currencyconverter.api.entity;

import java.time.LocalDate;
import java.util.Map;

public class Rates {
	
	private final String base;
	private final LocalDate date;
	private final Map<String, Float> rates;
	
	public Rates(String base, LocalDate date, Map<String, Float> rates) {
		super();
		this.base = base;
		this.date = date;
		this.rates = rates;
	}
	
	public String getBase() {
		return base;
	}
	public LocalDate getDate() {
		return date;
	}
	public Map<String, Float> getRates() {
		return rates;
	}
	
}
