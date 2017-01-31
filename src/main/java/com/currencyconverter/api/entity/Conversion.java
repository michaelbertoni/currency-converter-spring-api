package com.currencyconverter.api.entity;

import java.math.BigDecimal;

/**
 * Entité résultat renvoyée lors de l'appel à l'API convert
 * @author micha
 *
 */
public class Conversion {

	private String currencyFrom;
	private String currencyTo;
	private BigDecimal rate;
	private BigDecimal amountFrom;
	private BigDecimal amountTo;

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

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(BigDecimal amountFrom) {
		this.amountFrom = amountFrom;
	}

	public BigDecimal getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(BigDecimal amountTo) {
		this.amountTo = amountTo;
	}

	public Conversion(String currencyFrom, String currencyTo, BigDecimal rate, BigDecimal amountFrom,
			BigDecimal amountTo) {
		super();
		this.currencyFrom = currencyFrom;
		this.currencyTo = currencyTo;
		this.rate = rate;
		this.amountFrom = amountFrom;
		this.amountTo = amountTo;
	}

	public Conversion() {

	}

}
