package com.museumsystem.museumserver.utlis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.museumsystem.museumserver.dto.ErrorResponseDto;
import com.museumsystem.museumserver.dto.SuccessResponseDto;

public class ResponseManager {

	public static ResponseEntity<String> createErrorResponse(HttpStatus status, ErrorResponseDto response){
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return ResponseEntity.status(status).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static ResponseEntity<String> createSuccessResponse(HttpStatus status, SuccessResponseDto response){
		ObjectMapper mapper = new ObjectMapper();

		try {
			return ResponseEntity.status(status).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
