package com.museumsystem.museumserver.dto;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessResponseDto {
	@JsonProperty("data")
	HashMap<String, String> messages;
	
	public HashMap<String, String> getMessages() {
		return messages;
	}

	public void setMessages(HashMap<String, String> messages) {
		this.messages = messages;
	}
}
