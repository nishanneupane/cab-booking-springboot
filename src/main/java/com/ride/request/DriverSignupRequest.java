package com.ride.request;

import com.ride.entity.License;
import com.ride.entity.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverSignupRequest {
	private String name;
	private String email;

	private String password;

	private String mobile;
	private Double latitide;
	private Double longitude;
	
	private License license;
	
	private Vehicle vehicle;
}
