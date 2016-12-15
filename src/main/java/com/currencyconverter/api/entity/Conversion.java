package com.currencyconverter.api.entity;

import java.math.BigDecimal;

public class Conversion {
	
	private String currencyFrom;
	private String currencyTo;
	private BigDecimal valueFrom;
	private BigDecimal valueTo;
	
	public Conversion(String currencyFrom, String currencyTo, BigDecimal valueFrom, BigDecimal valueTo) {
		super();
		this.currencyFrom = currencyFrom;
		this.currencyTo = currencyTo;
		this.valueFrom = valueFrom;
		this.valueTo = valueTo;
	}
	
	public String getCurrencyFrom() {
		return currencyFrom;
	}
	public void setCurrencyFrom(String currencyFrom) {
		this.currencyFrom = currencyFrom;
	}
	public String getCurrencyTo() {
		return currencyTo;
	}
	public void setCurrencyTo(String currencyTo) {
		this.currencyTo = currencyTo;
	}
	public BigDecimal getValueFrom() {
		return valueFrom;
	}
	public void setValueFrom(BigDecimal valueFrom) {
		this.valueFrom = valueFrom;
	}
	public BigDecimal getValueTo() {
		return valueTo;
	}
	public void setValueTo(BigDecimal valueTo) {
		this.valueTo = valueTo;
	}
	
}
