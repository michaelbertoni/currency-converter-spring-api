package com.currencyconverter.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.currencyconverter.api.entity.CustomExchangeRate;

@Repository
public interface CustomExchangeRateRepository extends MongoRepository<CustomExchangeRate, String>{
	
	CustomExchangeRate findByBaseCurrencyCodeAndTermCurrencyCode(String baseCurrencyCode, String termCurrencyCode);

}
