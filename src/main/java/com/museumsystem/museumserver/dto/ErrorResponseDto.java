package com.museumsystem.museumserver.dto;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponseDto {

	private String message;
	private String code;
	@JsonProperty("details")
	HashMap<String, String> errors;
	
	public HashMap<String, String> getErrors() {
		return errors;
	}
	public void setErrors(HashMap<String, String> errors) {
		this.errors = errors;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
