package com.currencyconverter.api.controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.inject.Inject;
import javax.money.UnknownCurrencyException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.currencyconverter.api.entity.Conversion;
import com.currencyconverter.api.entity.ErrorResponse;
import com.currencyconverter.api.service.ConverterService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class ConverterController {

	@Inject
	private ConverterService converterService;

	@RequestMapping("/convert")
	public Conversion convert(@RequestParam(name = "currencyFrom", required = true) String currencyFrom,
			@RequestParam(name = "currencyTo", required = true) String currencyTo,
			@RequestParam(name = "valueFrom", required = true) BigDecimal valueFrom)
			throws JsonGenerationException, JsonMappingException, IOException {
		return this.converterService.convert(currencyFrom, currencyTo, valueFrom);
	}

	@ExceptionHandler(UnknownCurrencyException.class)
	public ResponseEntity<ErrorResponse> unknownCurrencyExceptiontionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.NOT_FOUND.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<ErrorResponse> numberFormatExceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

}
