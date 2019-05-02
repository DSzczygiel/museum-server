package com.museumsystem.museumserver.utlis;

import java.io.UnsupportedEncodingException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.museumsystem.museumserver.model.Ticket;

public class JWTTokenManager {

	private static final String ISSUER = "MuseumSystem";
	
	public static String generateEmailActivationToken(String email, String secret) throws IllegalArgumentException, UnsupportedEncodingException{
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create().withIssuer(ISSUER).withClaim("email", email).sign(algorithm);
			return token;

		} catch (JWTCreationException exception) {
			// Invalid Signing configuration / Couldn't convert Claims.
		}
		
		return null;
	}
	
	
	public static String generateTicketToken(Ticket ticket, String secret) {
		Algorithm algorithm;
		try {
			algorithm = Algorithm.HMAC256(secret);
			
			String token = JWT.create().withIssuer(ISSUER)
					.withClaim("ticket_id", ticket.getId())
					.sign(algorithm);
			return token;
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public static String generateToken(String secret) throws IllegalArgumentException, UnsupportedEncodingException {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create().withIssuer(ISSUER).sign(algorithm);
			return token;

		} catch (JWTCreationException exception) {
			// Invalid Signing configuration / Couldn't convert Claims.
		}
		
		return null;
	}
	
	public static boolean isTokenValid(String token, String secret) throws IllegalArgumentException, UnsupportedEncodingException {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer(ISSUER)
		        .build(); //Reusable verifier instance
		    DecodedJWT jwt = verifier.verify(token);
		    return true;
		} catch (JWTVerificationException exception){
			return false;
		    //Invalid signature/claims
		}
	}
	
	public static String getClaim(String token, String claim) {
		try {
		    DecodedJWT jwt = JWT.decode(token);
			String mClaim = jwt.getClaim(claim).asString();
			
			if(mClaim == null)
				return null;
			else
				return mClaim;
		    
		} catch (JWTDecodeException exception){
		    //Invalid token
			return null;
		}
	}

}
