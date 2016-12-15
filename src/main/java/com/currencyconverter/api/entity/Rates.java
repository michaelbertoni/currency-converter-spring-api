package com.currencyconverter.api.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class Rates {
	
	private final String base;
	private final LocalDate date;
	private final Map<String, BigDecimal> rates;
	
	public Rates(String base, LocalDate date, Map<String, BigDecimal> rates) {
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
	public Map<String, BigDecimal> getRates() {
		return rates;
	}
	
}
