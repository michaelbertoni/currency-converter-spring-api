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

	/**
	 * Convertir un montant @valueFrom d'une devise @currencyFrom vers une
	 * devise @currencyTo avec mise à jour du taux si sa dernière mise à jour
	 * est antérieure à la date du jour
	 * 
	 * @param currencyFrom
	 * @param currencyTo
	 * @param valueFrom
	 * @return
	 * @throws FixerIoServerException
	 * @throws CurrencyNotFoundException
	 */
	public Conversion convert(String currencyFrom, String currencyTo, BigDecimal valueFrom)
			throws FixerIoServerException, CurrencyNotFoundException {
		// Si la devise de départ est la même que celle d'arrivée, le montant de départ = montant d'arrivée
		if (currencyFrom.equals(currencyTo)) {
			return new Conversion(currencyFrom, currencyTo, new BigDecimal(1), valueFrom, valueFrom);
		}
		
		// Recherche des devises dans la base, renvoie une erreur si elles ne sont pas trouvées
		List<Currency> currencyList = this.currencyService.findAllCurrencies();
		if (currencyList.stream().map(Currency::getCode).noneMatch(c -> c.equals(currencyTo))
				|| currencyList.stream().map(Currency::getCode).noneMatch(c -> c.equals(currencyFrom))) {
			throw new CurrencyNotFoundException();
		}
		
		// Recherche du taux de conversion à partir du code des deux devises
		CustomExchangeRate customExchangeRate = this.customExchangeRateRepository
				.findByBaseCurrencyCodeAndTermCurrencyCode(currencyFrom, currencyTo);

		
		// Si le taux n'existe pas ou si sa dernière mise à jour est antérieure à la date du jour, mettre à jour le taux
		if (customExchangeRate == null || customExchangeRate.getRate() == null
				|| customExchangeRate.getLastUpdate().isBefore(LocalDate.now())) {
			customExchangeRate = refreshRate(currencyFrom, currencyTo);
		}

		BigDecimal rate = customExchangeRate != null ? customExchangeRate.getRate() : null;

		// Retourne un objet Conversion avec le montant d'arrivée calculé
		return new Conversion(currencyFrom, currencyTo, rate, valueFrom,
				rate != null ? valueFrom.multiply(rate) : null);
	}

	/**
	 * Mise à jour du taux de conversion d'une devise de départ vers une devise d'arrivée
	 * @param baseCurrencyCode
	 * @param termCurrencyCode
	 * @return
	 * @throws FixerIoServerException
	 */
	public CustomExchangeRate refreshRate(String baseCurrencyCode, String termCurrencyCode)
			throws FixerIoServerException {

		// Recherche d'un taux de conversion existant en base
		CustomExchangeRate customExchangeRate = this.customExchangeRateRepository
				.findByBaseCurrencyCodeAndTermCurrencyCode(baseCurrencyCode, termCurrencyCode);
		// S'il n'existe pas, le créer
		if (customExchangeRate == null) {
			customExchangeRate = new CustomExchangeRate();
			customExchangeRate.setBaseCurrency(baseCurrencyCode);
			customExchangeRate.setTermCurrency(termCurrencyCode);
		}

		// Appel de l'api du site fixer.io pour mettre à jour le taux
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

	/**
	 * Mise à jour de tous les taux de conversion depuis une devise de base
	 * @param baseCurrencyCode
	 * @return
	 * @throws FixerIoServerException
	 */
	public List<CustomExchangeRate> refreshRates(String baseCurrencyCode) throws FixerIoServerException {

		// Recherche des devises en base
		List<Currency> allCurrencies = this.currencyService.findAllCurrencies();
		
		// Création d'une liste de taux de conversion
		List<CustomExchangeRate> allExchangeRates = new ArrayList<>();

		// Appel de l'api fixer.io pour obtenir tous les taux à jour
		String json = null;
		try {
			json = readUrl("http://api.fixer.io/latest?base=" + baseCurrencyCode);
		} catch (Exception e) {
			throw new FixerIoServerException(e);
		}

		Gson gson = new Gson();

		if (!Objects.isNull(json)) {
			JsonObject jsonRates = gson.fromJson(json, JsonObject.class);
			// Itération sur l'ensemble des devises
			for (Currency currency : allCurrencies) {

				JsonElement rateJsonElement = jsonRates.getAsJsonObject("rates").get(currency.getCode());
				if (Objects.isNull(rateJsonElement)) {
					continue;
				}

				// Recherche en base du taux de conversion pour la devise en cours (arrivée) et la devise de base (départ)
				CustomExchangeRate customExchangeRate = this.customExchangeRateRepository
						.findByBaseCurrencyCodeAndTermCurrencyCode(baseCurrencyCode, currency.getCode());
				// Création de l'objet si la devise n'existe pas
				if (customExchangeRate == null) {
					customExchangeRate = new CustomExchangeRate();
					customExchangeRate.setBaseCurrency(baseCurrencyCode);
					customExchangeRate.setTermCurrency(currency.getCode());
				}

				// Mise à jour des taux et de la date d'actualisation, enregistrement en base
				customExchangeRate.setRate(rateJsonElement.getAsBigDecimal());
				customExchangeRate.setLastUpdate(LocalDate.parse(jsonRates.get("date").getAsString()));
				allExchangeRates.add(this.customExchangeRateRepository.save(customExchangeRate));
			}
		}

		return allExchangeRates;
	}

	/**
	 * Retourne en String le résultat d'une requête à partir d'une URL
	 * @param urlString
	 * @return
	 * @throws Exception
	 */
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
