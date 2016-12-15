package com.currencyconverter.api.controller;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.currencyconverter.api.service.ConverterService;

@RestController
public class ConverterController {
	
	@Inject
	private ConverterService converterService;
	
	@RequestMapping("/convert")
	public String convert(@RequestParam(name = "currencyFrom", required = true) String currencyFrom, 
			@RequestParam(name = "currencyTo", required = true) String currencyTo, 
			@RequestParam(name = "valueFrom", required = true) Double valueFrom) {
		return this.converterService.convertCurrency(currencyFrom, currencyTo, valueFrom);
	}

}
