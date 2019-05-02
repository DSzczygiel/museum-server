package com.museumsystem.museumserver.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.museumsystem.museumserver.service.ActivateUserAccountService;
import com.museumsystem.museumserver.utlis.JWTTokenManager;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	ActivateUserAccountService activateUserAccountService;
	/**
	 * This method activates customer's account and checks, if it's already activated or doesn't exist
	 * @param token JWT token with customer data
	 * @return Name of view to be displayed with appropriate message
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/activate")
	public String activateUserAccount(@RequestParam(value = "token", defaultValue = "null") String token) {
		try {
			System.out.println(token);
			System.out.println(JWTTokenManager.isTokenValid(token, "qwerty"));
			if (JWTTokenManager.isTokenValid(token, "qwerty") && !token.isEmpty() && token != null) {
				String email = JWTTokenManager.getClaim(token, "email");
				// TODO check function response
				int resp = activateUserAccountService.activateUser(email);
				if (resp == -1) {
					// user not exist
					return "accverificationuserdoesntexist";
				} else if (resp == -2) {
					// user arleady active
					return "accverificationuseralreadyactivated";
				} else {
					return "accverificationsuccess";
					// user not active
				}
			} else {
				return "accverificationinvalidtoken";
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
