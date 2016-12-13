package com.currencyconverter.api.entity;

public class Currency {
	
	public final long id;
	public final String code;
	public final String name;
	
	public Currency(long id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
}
