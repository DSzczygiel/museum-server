package com.museumsystem.museumserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoders {

	@Bean
	public PasswordEncoder clientPasswordEncoder() {
		return new BCryptPasswordEncoder(4);
	}

	@Bean
	public PasswordEncoder userPasswordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
}
