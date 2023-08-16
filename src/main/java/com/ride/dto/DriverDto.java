package com.ride.dto;

import com.ride.entity.UserRole;
import com.ride.entity.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {
	private Integer id;
	private String name;
	private String email;
	private String mobile;
	private Double rating;
	private Double latitude;
	private Double longitude;
	private UserRole role;
	private Vehicle vehicle;

}
