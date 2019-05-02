package com.museumsystem.museumserver.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	public static final String ROLE_EMP = "EMPLOYEE";
	public static final String ROLE_CUST = "CUSTOMER";

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/user/add").permitAll()
            .antMatchers("/account/activate").permitAll()
            .antMatchers("/oauth/token").permitAll()
            .antMatchers("/customer/**").hasRole(ROLE_CUST)
            .antMatchers("/employee/**").hasRole(ROLE_EMP)
            .antMatchers("/payment").hasRole(ROLE_CUST)
            .antMatchers(HttpMethod.GET, "/artwork/**").permitAll()
            .antMatchers(HttpMethod.POST, "/artwork/**").hasRole(ROLE_EMP)
            .antMatchers(HttpMethod.DELETE, "/artwork/**").hasRole(ROLE_EMP)
            .antMatchers("/artwork/maintenance/**").hasRole(ROLE_EMP)
            .antMatchers("/artwork/operation/**").hasRole(ROLE_EMP)
            .antMatchers(HttpMethod.GET, "/artist/**").permitAll()
            .antMatchers(HttpMethod.POST, "/artist/**").hasRole(ROLE_EMP)
            .antMatchers(HttpMethod.DELETE, "/artist/**").hasRole(ROLE_EMP)
            .antMatchers(HttpMethod.GET, "/news/**").permitAll()
            .antMatchers(HttpMethod.POST, "/news/**").hasRole(ROLE_EMP)
            .antMatchers(HttpMethod.DELETE, "/news/**").hasRole(ROLE_EMP)
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
