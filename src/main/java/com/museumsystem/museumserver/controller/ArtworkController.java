package com.museumsystem.museumserver.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.museumsystem.museumserver.dto.ArtworkDto;
import com.museumsystem.museumserver.dto.ErrorResponseDto;
import com.museumsystem.museumserver.service.ArtworkService;
import com.museumsystem.museumserver.utlis.JWTTokenManager;
import com.museumsystem.museumserver.utlis.ResponseManager;

@Controller
@RequestMapping("/artwork")
public class ArtworkController {
	@Autowired
	ArtworkService artworkService;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/{lang}", produces = { "application/json" })
	public ResponseEntity<String> getArtwork(@PathVariable("id") Long id, @PathVariable("lang") String lang) {

		if (!lang.equals("pl"))
			lang = "en";

		ArtworkDto artwork = artworkService.getArtwork(id, lang);

		if (artwork == null) {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_no_artwork");
			response.setMessage("Artwork doesn't exist");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(artwork));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ErrorResponseDto response = new ErrorResponseDto();
		response.setCode("error_unknown");
		response.setMessage("Unknown error");
		return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{lang}", produces = { "application/json" })
	public ResponseEntity<String> getArtworks(@PathVariable("lang") String lang) {

		if (!lang.equals("pl"))
			lang = "en";

		List<ArtworkDto> artworks = artworkService.getArtworks(lang);

		if (artworks == null) {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_no_artworks");
			response.setMessage("There are no artworks");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(artworks));

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
	public ResponseEntity<String> updateArtwork(@PathVariable("lang") String lang, @PathVariable("id") Long id,
			@RequestBody ArtworkDto requestData) {
		System.out.println(requestData.getArtist().toString());
		boolean status = artworkService.updateArtwork(lang, id, requestData);
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
	public ResponseEntity<String> addArtwork(@PathVariable("lang") String lang, @RequestBody ArtworkDto requestData,
			OAuth2Authentication auth) {
		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		String accessToken = details.getTokenValue();
		String email = JWTTokenManager.getClaim(accessToken, "user_name");

		boolean status = artworkService.addArtwork(lang, requestData, email);
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
		boolean status = artworkService.deleteArtwork(id);
		if (status) {
			return ResponseEntity.status(HttpStatus.OK).body("{}");
		} else {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_unknown");
			response.setMessage("Unknown error");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/operation/{id}" }, consumes = {
			"application/json" }, produces = { "application/json" })
	public ResponseEntity<String> addOperation(@PathVariable("id") Long id,
			@RequestBody HashMap<String, String> requestData, OAuth2Authentication auth) {

		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		String accessToken = details.getTokenValue();
		String email = JWTTokenManager.getClaim(accessToken, "user_name");

		boolean status = artworkService.addOperation(id, email, requestData.get("date"),
				requestData.get("description"));
		if (status) {
			return ResponseEntity.status(HttpStatus.OK).body("{}");
		} else {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_unknown");
			response.setMessage("Unknown error");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/operation/{id}" }, produces = { "application/json" })
	public ResponseEntity<String> getArtworkOperations(@PathVariable("id") Long id) {

		List<HashMap<String, String>> operations = artworkService.getArtworkOperations(id);
		if (operations != null) {

			ObjectMapper mapper = new ObjectMapper();

			try {
				return ResponseEntity.status(HttpStatus.OK)
						.body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(operations));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body("{}");
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/maintenance/{email}" }, produces = { "application/json" })
	public ResponseEntity<String> getArtworkMaintenances(@PathVariable("email") String email,
			OAuth2Authentication auth) {
		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		String accessToken = details.getTokenValue();
		String userEmail = JWTTokenManager.getClaim(accessToken, "user_name");
		if (userEmail.equals(email)) {
			List<HashMap<String, String>> maintenances = artworkService.getMaintenances(email);
			if (maintenances != null) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					return ResponseEntity.status(HttpStatus.OK)
							.body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(maintenances));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body("{}");
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/maintenance/{id}" }, produces = { "application/json" })
	public ResponseEntity<String> getArtworkMaintenances(@PathVariable("id") Long id) {
		boolean status = artworkService.addMaintenance(id);
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
