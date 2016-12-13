package com.currencyconverter.api.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.currencyconverter.api.entity.Rates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

@Service
public class ConverterService {
	
	private String url = "http://api.fixer.io/latest";
	
	public Float convertCurrency(String currencyFrom, String currencyTo, Float valueFrom) {
		Rates rates;
		try {
			rates = readRates();
			
			
			if (currencyFrom.equals(rates.getBase())) {
				return valueFrom * rates.getRates().get(currencyTo);
			}
			
			if (currencyTo.equals(rates.getBase())) {
				return valueFrom / rates.getRates().get(currencyFrom);
			}
			
			return valueFrom * (rates.getRates().get(currencyTo) / rates.getRates().get(currencyFrom));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JsonElement readJsonFromUrl(String requestUrl) throws IOException {
		URL url = new URL(requestUrl);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		JsonParser jp = new JsonParser();
		return jp.parse(new InputStreamReader((InputStream) request.getContent()));
	}
	
	public void getLastRates() throws IOException {
		Gson gson = new Gson();
		JsonElement jsonElement = readJsonFromUrl(this.url);
		File jsonFile = new File("rates.json");
		if (!jsonFile.exists()) {
			jsonFile.createNewFile();
		}
		Writer writer = new FileWriter(jsonFile);
		gson.toJson(jsonElement, writer);
		writer.close();
	}
	
	public Rates readRates() throws IOException {
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
		    @Override
		    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		            throws JsonParseException {
		    	String dateString = json.getAsString();
		    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        return LocalDate.parse(dateString, dtf);
		    }
		}).create();

		try (Reader reader = new FileReader("rates.json")) {
			Rates rates = gson.fromJson(reader, Rates.class);
			if (rates.getDate().isBefore(LocalDate.now())) {
				getLastRates();
	            return readRates();
			}
			return rates;
		} catch (IOException e) {
            getLastRates();
            return readRates();
        }
	}

}