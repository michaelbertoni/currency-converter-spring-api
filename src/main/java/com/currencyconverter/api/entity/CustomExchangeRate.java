package com.currencyconverter.api.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;

public class CustomExchangeRate {
	
	@Id
	private String id;

	private String baseCurrencyCode;
	private String termCurrencyCode;
	private BigDecimal rate;
	
	private LocalDate lastUpdate;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBaseCurrency() {
		return baseCurrencyCode;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrencyCode = baseCurrency;
	}

	public String getTermCurrency() {
		return termCurrencyCode;
	}

	public void setTermCurrency(String termCurrency) {
		this.termCurrencyCode = termCurrency;
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
		result = prime * result + ((baseCurrencyCode == null) ? 0 : baseCurrencyCode.hashCode());
		result = prime * result + ((termCurrencyCode == null) ? 0 : termCurrencyCode.hashCode());
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
		if (baseCurrencyCode == null) {
			if (other.baseCurrencyCode != null)
				return false;
		} else if (!baseCurrencyCode.equals(other.baseCurrencyCode))
			return false;
		if (termCurrencyCode == null) {
			if (other.termCurrencyCode != null)
				return false;
		} else if (!termCurrencyCode.equals(other.termCurrencyCode))
			return false;
		return true;
	}
	
}
