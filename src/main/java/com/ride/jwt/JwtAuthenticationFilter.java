package com.ride.jwt;

import java.io.IOException;
import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt=request.getHeader(JwtConstant.JWT_HEADER);
		
		if(jwt==null||!jwt.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			jwt=jwt.substring(7);
			SecretKey key=Keys.hmacShaKeyFor(JwtConstant.JWT_KEY.getBytes());
			Claims claims=Jwts
					.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(jwt)
					.getBody();
			
			String username=String.valueOf(claims.get("email"));
			String authorities=String.valueOf(claims.get("authorities"));
			
			java.util.List<GrantedAuthority> auths=AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
			
			Authentication auth=new UsernamePasswordAuthenticationToken(username, null,auths);
			
			SecurityContextHolder.getContext().setAuthentication(auth);
			
		} catch (Exception e) {
			throw new BadCredentialsException("something went wrong on filter ");
		}
		
		filterChain.doFilter(request, response);
	}
	
	
//	protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException{
//		return req.getServletPath().equals("/api/auth/user")
//	}


}













//Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
//
//if(authentication!=null) {
//	SecretKey key=Keys.hmacShaKeyFor(JwtConstant.JWT_KEY.getBytes());
//	
//	String jwt=Jwts.builder()
//			.setIssuer(JwtConstant.JWT_ISSUER)
//			.setIssuedAt(new Date())
//			.setExpiration(new Date(new Date().getTime()+JwtConstant.JWT_EXP_TIME))
//			.claim("authorities", populateAuthorities(authentication.getAuthorities()))
//			.claim("email", authentication.getName())
//			.signWith(key)
//			.compact();
//	
//}else {
//	filterChain.doFilter(request, response);
//	return;
//}





//public String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
//	
//	Set<String> authoritiesSet=new HashSet<>();
//	for(GrantedAuthority authority:collection) {
//		authoritiesSet.add(authority.getAuthority());
//		
//		
//	}
//	return String.join(",", authoritiesSet);
//}
