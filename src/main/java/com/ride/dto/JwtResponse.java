package com.ride.dto;

import com.ride.entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	private String jwt;
	private String message;
	private boolean isAuthenticated;
	private boolean isError;
	private String errorDetails;
	private UserRole role;

}
