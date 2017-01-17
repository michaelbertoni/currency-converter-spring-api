package com.currencyconverter.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class CurrencyConverterSpringApiApplication extends SpringBootServletInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CurrencyConverterSpringApiApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(CurrencyConverterSpringApiApplication.class, args);
	}
}
