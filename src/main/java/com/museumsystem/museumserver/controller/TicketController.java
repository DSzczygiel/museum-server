package com.museumsystem.museumserver.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.museumsystem.museumserver.dto.ErrorResponseDto;
import com.museumsystem.museumserver.dto.SuccessResponseDto;
import com.museumsystem.museumserver.model.Ticket;
import com.museumsystem.museumserver.service.TicketService;
import com.museumsystem.museumserver.utlis.JWTTokenManager;
import com.museumsystem.museumserver.utlis.ResponseManager;

@RestController
public class TicketController {

	@Autowired
	TicketService ticketService;

	@RequestMapping(method = RequestMethod.POST, value = "/customer/ticket", consumes = { "application/json" }, produces = {
	"application/json" })
	public ResponseEntity<String> addTicket(@RequestBody HashMap<String, String> requestData) {

		if (requestData.containsKey("customer_email")) {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Ticket ticket = mapper.convertValue(requestData, Ticket.class);
			Ticket newTicket = ticketService.addTicket(ticket, requestData.get("customer_email"));

			if (newTicket != null) {
				System.out.println(ticket.toString());
				try {
					return ResponseEntity.status(HttpStatus.OK)
							.body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ticket));

				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		ErrorResponseDto response = new ErrorResponseDto();
		response.setCode("error");
		response.setMessage("Ticket ordering failed");
		return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/customer/ticket", produces = { "application/json" })
	public ResponseEntity<String> getTicket(@RequestParam("id") Long id) {
		Ticket ticket = ticketService.getTicketById(id);

		if (ticket == null) {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_no_ticket");
			response.setMessage("Ticket doesn't exist");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		} else {

		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/customer/ticket/qr/{id}/{email}", produces = { "application/json" })
	public ResponseEntity<String> getTicketQr(@PathVariable("id") Long id, @PathVariable("email") String email) {
		String token = ticketService.generateTicketCode(id, email);

		if (token.equals("err_no_ticket")) {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_no_ticket");
			response.setMessage("Ticket doesn't exist");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		} else if (token.equals("err_no_customer")) {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_no_customer");
			response.setMessage("Customer doesn't exist");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		} else if (token.equals("err_invalid_data")) {
			ErrorResponseDto response = new ErrorResponseDto();
			response.setCode("error_invalid_data");
			response.setMessage("Code cannot be generated");
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		} else {
			SuccessResponseDto response = new SuccessResponseDto();
			HashMap<String, String> msg = new HashMap<>();
			msg.put("token", token);
			response.setMessages(msg);
			return ResponseManager.createSuccessResponse(HttpStatus.OK, response);
		}
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/customer/ticket/user/{email}", produces = { "application/json" })
	public ResponseEntity<String> getUserTickets(@PathVariable("email") String email, OAuth2Authentication auth) {
		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
		String accessToken = details.getTokenValue();
		System.out.println(accessToken);
		if (!JWTTokenManager.getClaim(accessToken, "user_name").equals(email))
			return ResponseEntity.status(HttpStatus.OK).body("{}");

		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets = ticketService.getCustomerTickets(email);
		if (tickets == null)
			return ResponseEntity.status(HttpStatus.OK).body("{}");

		ObjectMapper mapper = new ObjectMapper();
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tickets));

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.OK).body("{}");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/employee/ticket/validate/{id}", produces = { "application/json" })
	public ResponseEntity<String> validateTicket(@PathVariable("id") Long id) {
		boolean status = ticketService.validateTicket(id);

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
