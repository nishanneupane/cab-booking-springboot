package com.ride.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ride {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private User user;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Driver driver;
	
	@JsonIgnore
	private List<Integer> declinedDrivers=new ArrayList<>();
	
	private Double pickupLatitude;
	private Double pickupLongitude;
	private Double destinationLatutude;
	private Double destinationLongitude;
	
	private Double distance;
	private Long duration;
	
	private String pickupArea;
	private String destinatinArea;
	
	private RideStatus status;
	
	private LocalDateTime startedTime;
	private LocalDateTime endTime;
	
	private Double fare;
	
	private int otp;
	
	@Embedded
	private PaymentDetails paymentDetails=new PaymentDetails();
	

}
