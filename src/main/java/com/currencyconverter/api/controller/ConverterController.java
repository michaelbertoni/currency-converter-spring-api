package com.currencyconverter.api.controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.currencyconverter.api.entity.Conversion;
import com.currencyconverter.api.entity.ErrorResponse;
import com.currencyconverter.api.exceptions.ConverterException;
import com.currencyconverter.api.service.ConverterService;

@RestController
public class ConverterController {
	
	@Inject
	private ConverterService converterService;
	
	@RequestMapping("/convert")
	public Conversion convert(@RequestParam(name = "currencyFrom", required = true) String currencyFrom, 
			@RequestParam(name = "currencyTo", required = true) String currencyTo, 
			@RequestParam(name = "valueFrom", required = true) BigDecimal valueFrom) throws ConverterException, IOException {
		BigDecimal valueTo = this.converterService.convertCurrency(currencyFrom, currencyTo, valueFrom);
		return new Conversion(currencyFrom, currencyTo, valueFrom, valueTo);
	}
	
	@ExceptionHandler(ConverterException.class)
	public ResponseEntity<ErrorResponse> converterExceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<ErrorResponse> ioExceptionHandler(Exception ex) {
		ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
