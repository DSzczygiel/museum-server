package com.museumsystem.museumserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.museumsystem.museumserver.dto.ArtistDto;
import com.museumsystem.museumserver.dto.ErrorResponseDto;
import com.museumsystem.museumserver.service.ArtistService;
import com.museumsystem.museumserver.utlis.ResponseManager;

@Controller
@RequestMapping("/artist")
public class ArtistController {
	@Autowired
	ArtistService artistService;

	@RequestMapping(method = RequestMethod.GET, value = "/{lang}", produces = { "application/json" })
	public ResponseEntity<String> getArtists(@PathVariable("lang") String lang) {
		if (!lang.equals("pl"))
			lang = "en";

		List<ArtistDto> artists = artistService.getArtists(lang);

		if (artists == null) {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_no_artists");
			response.setMessage("There are no artists");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(artists));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(artwork.toString());
		ErrorResponseDto response = new ErrorResponseDto();
		response.setCode("error_unknown");
		response.setMessage("Unknown error");
		return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);

	}

	@RequestMapping(method = RequestMethod.POST, value = { "/{lang}/{id}" }, consumes = {
			"application/json" }, produces = { "application/json" })
	public ResponseEntity<String> updateArtist(@PathVariable("lang") String lang, @PathVariable("id") Long id,
			@RequestBody ArtistDto requestData) {
		System.out.println(requestData.toString());
		boolean status = artistService.updateArtist(lang, id, requestData);
		if (status) {
			return ResponseEntity.status(HttpStatus.OK).body("{}");
		} else {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_unknown");
			response.setMessage("Unknown error");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/{lang}" }, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<String> addArtist(@PathVariable("lang") String lang, @RequestBody ArtistDto requestData) {
		System.out.println(requestData.toString());
		boolean status = artistService.addArtist(lang, requestData);
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
	public ResponseEntity<String> deleteArtist(@PathVariable("id") Long id) {
		boolean status = artistService.deleteArtist(id);
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
