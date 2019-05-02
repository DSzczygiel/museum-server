package com.museumsystem.museumserver.security.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.museumsystem.museumserver.security.CustomTokenEnhancer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	static final String CLIENT_ID = "museum-client-app";
	static final String CLIENT_SECRET = "secret";
	static final String GRANT_TYPE_PASSWORD = "password";
    static final String REFRESH_TOKEN = "refresh_token";
	static final String SCOPE_READ = "read";
	static final String SCOPE_WRITE = "write";
    static final String SCOPE_TRUST = "trust";
    static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
	static final int ACCESS_TOKEN_VALIDITY_SECONDS = 10*60;
    static final int REFRESH_TOKEN_VALIDITY_SECONDS = 24*60*60;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder clientPasswordEncoder;
	
	@Autowired
	private UserDetailsService userDetailsService;

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        return defaultTokenServices;
}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("qwerty");
        return converter;
	}

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
    
    @Bean
    public TokenEnhancer tokenEnhancer() {
    	return new CustomTokenEnhancer();
    }

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer
				.inMemory()
				.withClient(CLIENT_ID)
				.secret(clientPasswordEncoder.encode((CLIENT_SECRET)))
				.authorizedGrantTypes(GRANT_TYPE_PASSWORD, REFRESH_TOKEN)
				.scopes(SCOPE_TRUST)
				.authorities(ROLE_CUSTOMER, ROLE_EMPLOYEE)
				.accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS).
				refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		
		endpoints.tokenStore(tokenStore())
			.tokenEnhancer(tokenEnhancerChain)
			.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService);
	}
	
    @Override
    public void configure(
      AuthorizationServerSecurityConfigurer oauthServer) 
      throws Exception {
        oauthServer
          .tokenKeyAccess("permitAll()")
          .checkTokenAccess("isAuthenticated()")
          .passwordEncoder(clientPasswordEncoder);
    }
}
