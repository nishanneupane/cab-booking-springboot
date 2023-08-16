package com.ride.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.ride.jwt.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/home").permitAll()
				.requestMatchers(HttpMethod.POST,"/api/v1/auth/**").permitAll()
				.requestMatchers(HttpMethod.POST,"/api/v1/auth/signup/**").permitAll()
//				.requestMatchers(HttpMethod.POST,"/api/v1/auth/login/**").permitAll()
				.anyRequest().authenticated()
			);
		
		http.csrf().disable()
		.addFilterBefore(new JwtAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.cors()
		.configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				
				CorsConfiguration configuration=new CorsConfiguration();
				configuration.setAllowedOrigins(
						Arrays.asList(
								"http://localhost:3000",
								"http://localhost:3001"
						
						));
//				configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
				configuration.setAllowedMethods(Collections.singletonList("*"));
				configuration.setAllowCredentials(true);
				configuration.setAllowedHeaders(Collections.singletonList("*"));
				configuration.setExposedHeaders(Arrays.asList("Authorization"));
				configuration.setMaxAge(3600L);
				return configuration;
			}
		});
//		.and()
//		.httpBasic()
//		.and()
//		.formLogin();
		
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
