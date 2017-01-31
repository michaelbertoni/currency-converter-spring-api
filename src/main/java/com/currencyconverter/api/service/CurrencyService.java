package com.currencyconverter.api.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
	
	@Inject
    private ResourceLoader resourceLoader;
	
	private static final Type REVIEW_TYPE = new TypeToken<List<Currency>>() {}.getType();
	
	public List<Currency> findAllCurrencies() {
		return this.currencyRepository.findByOrderByCodeAsc();
	}
	
	/**
	 * Chargement au lancement de l'application des devises depuis le fichier currencies.json dans la base
	 * @throws IOException 
	 */
	@PostConstruct
	public void loadData() throws IOException {
		Resource resource = resourceLoader.getResource("classpath:data/currencies.json");
		BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(br);
		List<Currency> listCurrencies = gson.fromJson(jsonReader, REVIEW_TYPE);
		
		this.currencyRepository.deleteAll();
		this.currencyRepository.save(listCurrencies);
	}

}
