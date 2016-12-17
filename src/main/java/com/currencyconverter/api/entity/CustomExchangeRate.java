package com.currencyconverter.api.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomExchangeRate implements Serializable {

	private static final long serialVersionUID = 6099307368366148445L;

	private String baseCurrency;
	private String termCurrency;
	private BigDecimal rate;
	
	private LocalDate lastUpdate;

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getTermCurrency() {
		return termCurrency;
	}

	public void setTermCurrency(String termCurrency) {
		this.termCurrency = termCurrency;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public LocalDate getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDate lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseCurrency == null) ? 0 : baseCurrency.hashCode());
		result = prime * result + ((termCurrency == null) ? 0 : termCurrency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomExchangeRate other = (CustomExchangeRate) obj;
		if (baseCurrency == null) {
			if (other.baseCurrency != null)
				return false;
		} else if (!baseCurrency.equals(other.baseCurrency))
			return false;
		if (termCurrency == null) {
			if (other.termCurrency != null)
				return false;
		} else if (!termCurrency.equals(other.termCurrency))
			return false;
		return true;
	}

	public CustomExchangeRate(String baseCurrency, String termCurrency, BigDecimal rate, LocalDate lastUpdate) {
		super();
		this.baseCurrency = baseCurrency;
		this.termCurrency = termCurrency;
		this.rate = rate;
		this.lastUpdate = lastUpdate;
	}
	
	public CustomExchangeRate() {
		
	}
	
}
