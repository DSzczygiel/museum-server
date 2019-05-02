package com.museumsystem.museumserver.controller;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.museumsystem.museumserver.dto.ErrorResponseDto;
import com.museumsystem.museumserver.dto.SuccessResponseDto;
import com.museumsystem.museumserver.model.Customer;
import com.museumsystem.museumserver.service.ActivateUserAccountService;
import com.museumsystem.museumserver.service.AddUserService;
import com.museumsystem.museumserver.service.EmailManagerService;
import com.museumsystem.museumserver.service.LoginUserService;
import com.museumsystem.museumserver.utlis.JWTTokenManager;
import com.museumsystem.museumserver.utlis.ResponseManager;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	AddUserService addUserService;
	@Autowired
	ActivateUserAccountService activateUserAccountService;
	@Autowired
	EmailManagerService emailManagerService;
	@Autowired
	LoginUserService loginUserService;
	@Autowired
	PasswordEncoder userPasswordEncoder;
	
	/**
	 * Function checks if user/employee with given email address exists and if request body is complete.
	 * If not, adds user to database and sends email with activation token and return.
	 * If yes, return 
	 * 
	 * @param user User object mapped from JSON request body
	 * @param lang language taken from device, used for email language
	 * @return code 200 on success, code 400 and error details in JSON response body
	 */
	
	@RequestMapping(method = RequestMethod.POST, value = "/add", consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<String> addUser(@RequestBody Customer user,
			@RequestParam(value = "lang", defaultValue = "pl_PL") String lang) {
		String token = null;
		String password = null;
		String dateString = null;

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		dateString = dateFormat.format(date);
		user.setRegisterDate(dateString);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Customer>> violations = validator.validate(user);

		if (violations.isEmpty()) {
			try {
				token = JWTTokenManager.generateEmailActivationToken(user.getEmail(), "qwerty");
			} catch (IllegalArgumentException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			password = userPasswordEncoder.encode(user.getPassword());
			user.setPassword(password);

			if (addUserService.addUser(user)) {
				emailManagerService.sendActivationEmail(user.getEmail(), user.getFirstName(), token, lang);
				SuccessResponseDto response = new SuccessResponseDto();
				return ResponseManager.createSuccessResponse(HttpStatus.OK, response);
			} else {
				ErrorResponseDto response = new ErrorResponseDto();
				response.setMessage("Email is already used");
				response.setCode("email_already_used");				

				return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
			}
		} else {
			ErrorResponseDto response = new ErrorResponseDto();
			HashMap<String, String> errors = new HashMap<String, String>();

			response.setCode("incomplete_data");
			response.setMessage("Incomplete form data");
			
			int i = 0;
			for (ConstraintViolation<Customer> violation : violations) {
				errors.put("error" + i, violation.getMessage());
				System.out.println(violation.getMessage());
				i++;
			}
			response.setErrors(errors);
			return ResponseManager.createErrorResponse(HttpStatus.BAD_REQUEST, response);
		}
	}
}
