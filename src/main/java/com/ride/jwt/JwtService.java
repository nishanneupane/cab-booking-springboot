package com.ride.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	SecretKey key=Keys.hmacShaKeyFor(JwtConstant.JWT_KEY.getBytes());
	
	
	public String generateToken(Authentication authentication) {
		String jwt=Jwts
				.builder()
				.setIssuer(JwtConstant.JWT_ISSUER)
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+JwtConstant.JWT_EXP_TIME))
				.claim("email", authentication.getName())
				.signWith(key)
				.compact();
		
		return jwt;
	}
	
	public String getUsernameFromJwt(String jwt) {
		jwt=jwt.substring(7);
		Claims claims=Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		String email=String.valueOf(claims.get("email"));
		
		return email;
	}

}
