package com.currencyconverter.api.entity;

import org.springframework.data.annotation.Id;

public class Currency {
	
	@Id
	private String id;
	
	private String code;
	
	private String libelle;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Override
	public String toString() {
		return code + " : " + libelle;
	}

}
