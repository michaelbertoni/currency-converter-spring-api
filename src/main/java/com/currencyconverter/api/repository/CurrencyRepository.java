package com.currencyconverter.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.currencyconverter.api.entity.Currency;

@Repository
public interface CurrencyRepository extends MongoRepository<Currency, String>{
	
	Currency findByCode(String code);
	
	List<Currency> findByOrderByCodeAsc();

}
