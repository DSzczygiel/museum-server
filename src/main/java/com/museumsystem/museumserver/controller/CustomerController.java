package com.museumsystem.museumserver.controller;

import java.util.HashMap;

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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.museumsystem.museumserver.dto.CustomerInfoDto;
import com.museumsystem.museumserver.dto.ErrorResponseDto;
import com.museumsystem.museumserver.service.CustomerService;
import com.museumsystem.museumserver.utlis.JWTTokenManager;
import com.museumsystem.museumserver.utlis.ResponseManager;

@RestController
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@RequestMapping(method = RequestMethod.GET, value = "/customer/{email}", produces = { "application/json" })
	public ResponseEntity<String> getUserInfo(@PathVariable("email") String email) {
		CustomerInfoDto custInfo = customerService.getCustomerInfo(email);
		if (custInfo != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				return ResponseEntity.status(HttpStatus.OK)
						.body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(custInfo));

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error");
			response.setMessage("Error");

			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		}

		ErrorResponseDto response = new ErrorResponseDto();
		response.setCode("err_unknown");
		response.setMessage("Unknown error");

		return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/customer/{email}/changedata", consumes = {
			"application/json" }, produces = { "application/json" })
	public ResponseEntity<String> changeUserData(@RequestBody HashMap<String, String> requestData,
			@PathVariable("email") String email, OAuth2Authentication auth) {
		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		String accessToken = details.getTokenValue();
		if (JWTTokenManager.getClaim(accessToken, "user_name").equals(email)) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);

			CustomerInfoDto custInfo = mapper.convertValue(requestData, CustomerInfoDto.class);

			String result = customerService.changeData(email, custInfo.getEmail(), custInfo.getFirstName(),
					custInfo.getLastName());

			if (result.equals("success")) {
				return ResponseEntity.status(HttpStatus.OK).body("{}");
			} else if (result.equals("err_email_used")) {
				ErrorResponseDto response = new ErrorResponseDto();
				response.setCode("err_email_used");
				response.setMessage("Email is already used");

				return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
			} else if (result.equals("err_incomplete_data")) {
				ErrorResponseDto response = new ErrorResponseDto();
				response.setCode("err_incomplete_data");
				response.setMessage("Incomplete data");

				return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
			} else {
				ErrorResponseDto response = new ErrorResponseDto();
				response.setCode("err_unknown");
				response.setMessage("Unknown error");

				return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
			}
		}

		ErrorResponseDto response = new ErrorResponseDto();
		response.setCode("err_unknown");
		response.setMessage("Unknown error");

		return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/customer/{email}/changepassword", consumes = {
			"application/json" }, produces = { "application/json" })
	public ResponseEntity<String> changeUserPassword(@RequestBody HashMap<String, String> requestData,
			@PathVariable("email") String email, OAuth2Authentication auth) {
		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		String accessToken = details.getTokenValue();
		if (JWTTokenManager.getClaim(accessToken, "user_name").equals(email)) {
			String oldPassword = requestData.get("old_password");
			String newPassword = requestData.get("new_password");

			if (oldPassword != null && newPassword != null) {
				String result = customerService.changePassword(email, oldPassword, newPassword);

				if (result.equals("success")) {
					return ResponseEntity.status(HttpStatus.OK).body("{}");
				} else if (result.equals("err_wrong_pass")) {
					ErrorResponseDto response = new ErrorResponseDto();
					response.setCode("err_wrong_pass");
					response.setMessage("Current password is incorrect");

					return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
				} else {
					ErrorResponseDto response = new ErrorResponseDto();
					response.setCode("err_unknown");
					response.setMessage("Unknown error");

					return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
				}
			}
		}
		ErrorResponseDto response = new ErrorResponseDto();
		response.setCode("err_unknown");
		response.setMessage("Unknown error");

		return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
	}

	@RequestMapping(method = {RequestMethod.DELETE}, value = {"/customer/{email}"}, produces = { "application/json" })
	public ResponseEntity<String> deleteCustomer(@PathVariable("email") String email, OAuth2Authentication auth){
		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		String accessToken = details.getTokenValue();
		if (JWTTokenManager.getClaim(accessToken, "user_name").equals(email)) {
			String result = customerService.deleteCustomer(email);
			
			if (result.equals("success")) {
				return ResponseEntity.status(HttpStatus.OK).body("{}");
			} else if (result.equals("err_no_user")) {
				ErrorResponseDto response = new ErrorResponseDto();
				response.setCode("err_no_user");
				response.setMessage("User with given email doesn't exist");

				return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
			} else {
				ErrorResponseDto response = new ErrorResponseDto();
				response.setCode("err_unknown");
				response.setMessage("Unknown error");

				return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
			}
		}
		ErrorResponseDto response = new ErrorResponseDto();
		response.setCode("err_unknown");
		response.setMessage("Unknown error");

		return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);	
	}
}
