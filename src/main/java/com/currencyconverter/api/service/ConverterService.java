package com.currencyconverter.api.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.json.JSONException;
import org.springframework.stereotype.Service;

import com.currencyconverter.api.entity.Conversion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

@Service
public class ConverterService {
	
	private String url = "http://api.fixer.io/latest?base=";
	
	public Conversion convertCurrency(String currencyFrom, String currencyTo, Float valueFrom) {
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			
			JsonElement jsonElement = readJsonFromUrl(this.url + currencyFrom);
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			
			Type type = new TypeToken<Map<String, Float>>(){}.getType();
			Map<String, Float> rates = gson.fromJson(jsonObject.get("rates"), type);
			Float valueTo = valueFrom * rates.get(currencyTo);
			return new Conversion(currencyFrom, currencyTo, valueFrom, valueTo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

}
