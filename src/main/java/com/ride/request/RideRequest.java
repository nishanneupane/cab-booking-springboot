package com.ride.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {
	
	private Double pickupLongitude;
	private Double pickupLatitude;
	private Double destinationLatitude;
	private Double destinationLongitude;
	private String pickupArea;
	private String destinationArea;

}
