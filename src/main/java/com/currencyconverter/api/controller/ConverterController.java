package com.currencyconverter.api.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.currencyconverter.api.entity.Conversion;
import com.currencyconverter.api.entity.Currency;
import com.currencyconverter.api.entity.CustomExchangeRate;
import com.currencyconverter.api.entity.ErrorResponse;
import com.currencyconverter.api.exception.CurrencyNotFoundException;
import com.currencyconverter.api.exception.FixerIoServerException;
import com.currencyconverter.api.service.CurrencyService;
import com.currencyconverter.api.service.CustomExchangeRateService;

@RestController
public class ConverterController {

	@Inject
	private CustomExchangeRateService converterService;

	@Inject
	private CurrencyService currencyService;

	/**
	 * API principale : convertir le montant @valueFrom d'une devise @currencyFrom vers une devise @currencyTo
	 * Retourne l'objet résultat Conversion
	 * @param currencyFrom
	 * @param currencyTo
	 * @param valueFrom
	 * @return
	 * @throws IOException
	 * @throws FixerIoServerException
	 * @throws CurrencyNotFoundException
	 */
	@RequestMapping("/convert")
	public Conversion convert(@RequestParam(name = "currencyFrom", required = true) String currencyFrom,
			@RequestParam(name = "currencyTo", required = true) String currencyTo,
			@RequestParam(name = "valueFrom", required = true) BigDecimal valueFrom)
			throws IOException, FixerIoServerException, CurrencyNotFoundException {
		return this.converterService.convert(currencyFrom, currencyTo, valueFrom);
	}

	/**
	 * Mise à jour de tous les taux de conversion à partir d'une devise de base @baseCurrencyCode
	 * @param baseCurrencyCode
	 * @return
	 * @throws FixerIoServerException
	 */
	@RequestMapping("/refreshRates")
	public List<CustomExchangeRate> refreshRates(
			@RequestParam(name = "baseCurrencyCode", required = true) String baseCurrencyCode)
			throws FixerIoServerException {
		return this.converterService.refreshRates(baseCurrencyCode);
	}

	/**
	 * Retourne la liste de toutes les devises en base
	 * @return
	 */
	@RequestMapping("/getAllCurrencies")
	public List<Currency> getAllCurrencies() {
		return this.currencyService.findAllCurrencies();
	}

	/**
	 * Erreur liée à l'API convert : devise introuvable en base
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(CurrencyNotFoundException.class)
	public ResponseEntity<ErrorResponse> currencyNotFoundException(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.NOT_FOUND.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Erreur liée à l'API convert et refreshRates : l'appel à l'API externe Fixer.io renvoie une erreur
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(FixerIoServerException.class)
	public ResponseEntity<ErrorResponse> fixerIoServerExceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Erreur liée à l'API convert : format incorrect du montant à convertir
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<ErrorResponse> numberFormatExceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

}
