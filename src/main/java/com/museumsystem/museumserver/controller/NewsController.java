package com.museumsystem.museumserver.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.museumsystem.museumserver.dto.ErrorResponseDto;
import com.museumsystem.museumserver.service.NewsService;
import com.museumsystem.museumserver.utlis.JWTTokenManager;
import com.museumsystem.museumserver.utlis.ResponseManager;

@RestController
@RequestMapping("/news")
public class NewsController {
	@Autowired
	NewsService newsService;


	@RequestMapping(method = RequestMethod.GET, value = { "/{lang}" }, produces = { "application/json" })
	public ResponseEntity<String> getNews(@PathVariable("lang") String lang) {
		if(!lang.equals("pl"))
			lang = "en";
		
		List<HashMap<String, String>> operations = newsService.getNews(lang);
		
		if (operations != null) {
			ObjectMapper mapper = new ObjectMapper();

			try {
				return ResponseEntity.status(HttpStatus.OK)
						.body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(operations));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body("{}");
	}
	
	@RequestMapping(method = RequestMethod.POST, value = { "/" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<String> addNews(@RequestBody HashMap<String, String> requestData, OAuth2Authentication auth) {

		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		String accessToken = details.getTokenValue();
		String email = JWTTokenManager.getClaim(accessToken, "user_name");

		boolean status = newsService.addNews(email, requestData);

		if (status) {
			return ResponseEntity.status(HttpStatus.OK).body("{}");
		} else {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_unknown");
			response.setMessage("Unknown error");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = { "/{id}" }, produces = { "application/json" })
	public ResponseEntity<String> deleteArtwork(@PathVariable("id") Long id) {
		boolean status = newsService.deleteNews(id);
		if (status) {
			return ResponseEntity.status(HttpStatus.OK).body("{}");
		} else {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_unknown");
			response.setMessage("Unknown error");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/{id}" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<String> updateNews(@PathVariable("id") Long id,
			@RequestBody HashMap<String, String> requestData, OAuth2Authentication auth) {

		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		String accessToken = details.getTokenValue();
		String email = JWTTokenManager.getClaim(accessToken, "user_name");
		String content = requestData.get("content");

		boolean status = newsService.updateNews(id, email, content);

		if (status) {
			return ResponseEntity.status(HttpStatus.OK).body("{}");
		} else {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_unknown");
			response.setMessage("Unknown error");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		}
	}
}
