package com.museumsystem.museumserver.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.museumsystem.museumserver.dto.ErrorResponseDto;
import com.museumsystem.museumserver.dto.SuccessResponseDto;
import com.museumsystem.museumserver.model.Payment;
import com.museumsystem.museumserver.service.PaymentService;
import com.museumsystem.museumserver.utlis.ResponseManager;

@Controller
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
	PaymentService paymentService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/new", consumes = { "application/json" }, produces = {
	"application/json" })
	public ResponseEntity<String> newPayment(@RequestBody HashMap<String, String> requestData) {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Payment payment = mapper.convertValue(requestData, Payment.class);
			
			if(payment != null) {
				paymentService.addPayment(payment);
				SuccessResponseDto response = new SuccessResponseDto();
				return ResponseManager.createSuccessResponse(HttpStatus.OK, response);
			}
		
		ErrorResponseDto response = new ErrorResponseDto();
		response.setCode("error");
		response.setMessage("Payment error");
		return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
	}
}
