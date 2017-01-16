package com.currencyconverter.api.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.currencyconverter.api.entity.Conversion;
import com.currencyconverter.api.entity.Currency;
import com.currencyconverter.api.entity.CustomExchangeRate;
import com.currencyconverter.api.exception.CurrencyNotFoundException;
import com.currencyconverter.api.exception.FixerIoServerException;
import com.currencyconverter.api.repository.CustomExchangeRateRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class CustomExchangeRateService {

	@Inject
	private CurrencyService currencyService;

	@Inject
	private CustomExchangeRateRepository customExchangeRateRepository;

	public Conversion convert(String currencyFrom, String currencyTo, BigDecimal valueFrom) throws FixerIoServerException, CurrencyNotFoundException {
		CustomExchangeRate customExchangeRate = this.customExchangeRateRepository
				.findByBaseCurrencyCodeAndTermCurrencyCode(currencyFrom, currencyTo);

		if (currencyFrom.equals(currencyTo)) {
			return new Conversion(currencyFrom, currencyTo, new BigDecimal(1), valueFrom, valueFrom);
		}
		
		List<Currency> currencyList = this.currencyService.findAllCurrencies();
		if (currencyList.stream().map(Currency::getCode).noneMatch(c -> c.equals(currencyTo)) ||
				currencyList.stream().map(Currency::getCode).noneMatch(c -> c.equals(currencyFrom))) {
			throw new CurrencyNotFoundException();
		}

		if (customExchangeRate == null || customExchangeRate.getRate() == null
				|| customExchangeRate.getLastUpdate().isBefore(LocalDate.now())) {
			customExchangeRate = refreshRate(currencyFrom, currencyTo);
		}
		
		BigDecimal rate = customExchangeRate != null ? customExchangeRate.getRate() : null;

		return new Conversion(currencyFrom, currencyTo, rate, valueFrom,
				rate != null ? valueFrom.multiply(rate) : null);
	}

	public CustomExchangeRate refreshRate(String baseCurrencyCode, String termCurrencyCode)
			throws FixerIoServerException {

		CustomExchangeRate customExchangeRate = this.customExchangeRateRepository
				.findByBaseCurrencyCodeAndTermCurrencyCode(baseCurrencyCode, termCurrencyCode);
		if (customExchangeRate == null) {
			customExchangeRate = new CustomExchangeRate();
			customExchangeRate.setBaseCurrency(baseCurrencyCode);
			customExchangeRate.setTermCurrency(termCurrencyCode);
		}

		String json = null;
		try {
			json = readUrl("http://api.fixer.io/latest?base=" + baseCurrencyCode + "&symbols=" + termCurrencyCode);
		} catch (Exception e) {
			throw new FixerIoServerException(e);
		}

		Gson gson = new Gson();

		if (!Objects.isNull(json)) {
			JsonObject jsonRates = gson.fromJson(json, JsonObject.class);
			JsonElement rateJsonElement = jsonRates.getAsJsonObject("rates").get(termCurrencyCode);
			if (Objects.isNull(rateJsonElement)) {
				return null;
			}

			customExchangeRate.setRate(rateJsonElement.getAsBigDecimal());
			customExchangeRate.setLastUpdate(LocalDate.parse(jsonRates.get("date").getAsString()));
		}

		return this.customExchangeRateRepository.save(customExchangeRate);
	}

	public List<CustomExchangeRate> refreshRates(String baseCurrencyCode) throws FixerIoServerException {

		List<Currency> allCurrencies = this.currencyService.findAllCurrencies();
		List<CustomExchangeRate> allExchangeRates = new ArrayList<>();

		String json = null;
		try {
			json = readUrl("http://api.fixer.io/latest?base=" + baseCurrencyCode);
		} catch (Exception e) {
			throw new FixerIoServerException(e);
		}

		Gson gson = new Gson();

		if (!Objects.isNull(json)) {
			JsonObject jsonRates = gson.fromJson(json, JsonObject.class);
			for (Currency currency : allCurrencies) {

				JsonElement rateJsonElement = jsonRates.getAsJsonObject("rates").get(currency.getCode());
				if (Objects.isNull(rateJsonElement)) {
					continue;
				}

				CustomExchangeRate customExchangeRate = this.customExchangeRateRepository
						.findByBaseCurrencyCodeAndTermCurrencyCode(baseCurrencyCode, currency.getCode());
				if (customExchangeRate == null) {
					customExchangeRate = new CustomExchangeRate();
					customExchangeRate.setBaseCurrency(baseCurrencyCode);
					customExchangeRate.setTermCurrency(currency.getCode());
				}

				customExchangeRate.setRate(rateJsonElement.getAsBigDecimal());
				customExchangeRate.setLastUpdate(LocalDate.parse(jsonRates.get("date").getAsString()));
				allExchangeRates.add(this.customExchangeRateRepository.save(customExchangeRate));
			}
		}

		return allExchangeRates;
	}

	private static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
}
