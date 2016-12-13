package com.currencyconverter.api.controller;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.currencyconverter.api.entity.Conversion;
import com.currencyconverter.api.service.ConverterService;

@RestController
public class ConverterController {
	
	@Inject
	private ConverterService converterService;
	
	@RequestMapping("/convert")
	public Conversion convert(@RequestParam("currencyFrom") String currencyFrom, @RequestParam("currencyTo") String currencyTo, @RequestParam("valueFrom") Float valueFrom) {
		return this.converterService.convertCurrency(currencyFrom, currencyTo, valueFrom);
	}

}
