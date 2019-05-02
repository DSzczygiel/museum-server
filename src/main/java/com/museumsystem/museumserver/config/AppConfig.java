package com.museumsystem.museumserver.config;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


@Configuration
public class AppConfig implements WebMvcConfigurer {
	
	/**
	 * Set path with translations
	 * @return messageSource
	 */
	@Bean
	public MessageSource messageSource() {
	     ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	     messageSource.setBasename("classpath:i18n/messages");
	     return messageSource;
	}
	
	 @Bean
	   public LocaleResolver localeResolver() {
	       SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
	       sessionLocaleResolver.setDefaultLocale(Locale.ENGLISH);
	       return sessionLocaleResolver;
	   }
	 
	   @Bean
	   public LocaleChangeInterceptor localeChangeInterceptor() {
	       LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	       lci.setParamName("lang");
	       return lci;
	   }
	 
	   @Override
	   public void addInterceptors(InterceptorRegistry registry) {
	       registry.addInterceptor(localeChangeInterceptor());
	   }
}
