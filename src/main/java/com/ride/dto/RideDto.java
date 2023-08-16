package com.ride.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ride.entity.Driver;
import com.ride.entity.PaymentDetails;
import com.ride.entity.RideStatus;
import com.ride.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideDto {
	private Integer id;
	
	private UserDto user;
	
	private DriverDto driver;
		
	private Double pickupLatitude;
	private Double pickupLongitude;
	private Double destinationLatutude;
	private Double destinationLongitude;
	
	private String pickupArea;
	private String destinatinArea;
	
	private Double distance;
	private Long duration;
	
	private RideStatus status;
	
	private LocalDateTime startedTime;
	private LocalDateTime endTime;
	
	private Double fare;
	
	private int otp;
	
	@Embedded
	private PaymentDetails paymentDetails=new PaymentDetails();

}
