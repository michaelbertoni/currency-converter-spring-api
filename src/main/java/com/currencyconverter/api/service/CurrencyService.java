package com.currencyconverter.api.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.currencyconverter.api.entity.Currency;
import com.currencyconverter.api.repository.CurrencyRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

@Service
public class CurrencyService {
	
	@Inject
	private CurrencyRepository currencyRepository;
	
	private static final Type REVIEW_TYPE = new TypeToken<List<Currency>>() {}.getType();
	
	public List<Currency> findAllCurrencies() {
		return this.currencyRepository.findAll();
	}
	
	@PostConstruct
	public void loadData() throws FileNotFoundException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("data/currencies.json").getFile());
		FileReader fr = new FileReader(file);
		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(fr);
		List<Currency> listCurrencies = gson.fromJson(jsonReader, REVIEW_TYPE);
		
		this.currencyRepository.deleteAll();
		this.currencyRepository.save(listCurrencies);
	}

}
