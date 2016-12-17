package com.currencyconverter.api.service;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.currencyconverter.api.entity.CustomExchangeRate;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

@Service
public class CustomExchangeRateService {

	private ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
			.registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());

	public void save(CustomExchangeRate customExchangeRate)
			throws JsonGenerationException, JsonMappingException, IOException {
		Set<CustomExchangeRate> customExchangeRateSet = findAll();
		if (!customExchangeRateSet.add(customExchangeRate)) {
			customExchangeRateSet.remove(customExchangeRate);
			customExchangeRateSet.add(customExchangeRate);
		}
		mapper.writeValue(new File("rates.json"), customExchangeRateSet);
	}

	public Set<CustomExchangeRate> findAll() throws JsonParseException, JsonMappingException, IOException {
		File file = new File("rates.json");
		if (!file.exists() || file.length() == 0) {
			file.createNewFile();
			return new HashSet<CustomExchangeRate>();
		}
		return mapper.readValue(file,
				mapper.getTypeFactory().constructCollectionType(Set.class, CustomExchangeRate.class));
	}
}
