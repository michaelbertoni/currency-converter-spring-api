package com.currencyconverter.api.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.convert.ExchangeRateType;
import org.springframework.stereotype.Service;

import com.currencyconverter.api.entity.Conversion;
import com.currencyconverter.api.entity.CustomExchangeRate;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class ConverterService {

	@Inject
	private CustomExchangeRateService customExchangeRateService;

	public Conversion convert(String currencyFrom, String currencyTo, BigDecimal amountFrom)
			throws JsonParseException, JsonMappingException, IOException {
		Set<CustomExchangeRate> customExchangeRateSet = this.customExchangeRateService.findAll();
		Optional<CustomExchangeRate> existingExchangeRate = customExchangeRateSet.stream()
				.filter(c -> c.getBaseCurrency().equals(currencyFrom) && c.getTermCurrency().equals(currencyTo))
				.findAny();

		if (existingExchangeRate.isPresent() && existingExchangeRate.get().getLastUpdate().isEqual(LocalDate.now())) {
			BigDecimal amountTo = amountFrom.multiply(existingExchangeRate.get().getRate());
			return new Conversion(currencyFrom, currencyTo, existingExchangeRate.get().getRate(), amountFrom, amountTo);
		} else {
			return convertWithECBRatesUpdate(currencyFrom, currencyTo, amountFrom);
		}

	}

	public Conversion convertWithECBRatesUpdate(String currencyFrom, String currencyTo, BigDecimal amountFromBigDecimal)
			throws JsonGenerationException, JsonMappingException, IOException {

		// Define the exchange rate provider to European Central Bank
		ExchangeRateProvider exchangeRateProvider = MonetaryConversions.getExchangeRateProvider(ExchangeRateType.ECB);

		// Define currency units for both base and term currency given
		CurrencyUnit currencyUnitFrom = Monetary.getCurrency(currencyFrom);
		CurrencyUnit currencyUnitTo = Monetary.getCurrency(currencyTo);

		// Convert base amount to term amount using the exchange rate provider
		// to retrieve the current rate to use
		Money moneyFrom = Money.of(amountFromBigDecimal, currencyUnitFrom);
		CurrencyConversion currencyConversion = exchangeRateProvider.getCurrencyConversion(currencyUnitTo);
		Money moneyTo = moneyFrom.with(currencyConversion);

		// Set output variables
		BigDecimal rate = currencyConversion.getExchangeRate(moneyFrom).getFactor().numberValue(BigDecimal.class);
		BigDecimal amountFrom = moneyFrom.getNumber().numberValue(BigDecimal.class);
		BigDecimal amountTo = moneyTo.getNumber().numberValue(BigDecimal.class);

		// Create CustomExchangeRate object to persist
		CustomExchangeRate customExchangeRate = new CustomExchangeRate(currencyFrom, currencyTo, rate, LocalDate.now());
		this.customExchangeRateService.save(customExchangeRate);

		// Return Conversion object to be serialized for JSON response
		return new Conversion(currencyFrom, currencyTo, rate, amountFrom, amountTo);
	}

}
