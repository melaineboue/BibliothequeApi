package com.melaineboue.bibliotheque.empruntlivre.Enums;

public enum EnumsBookStatus {
	AVAILABLE("Available"),
	USED("Used");
	
	String code;
	
	EnumsBookStatus(String code)
	{
		this.code = code;
	}
	
	public String getCode(){ return code;}

}
