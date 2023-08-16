package com.ride.dto;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class Calculator {
	
	private static final Integer EARTH_RADIUS=6371;
	
	public Double calculateDistance(Double srcLatitude,Double srcLongitude,Double desLatitude,Double desLongitude) {
		Double dlat=Math.toRadians(desLatitude-srcLatitude);
		Double dlon=Math.toRadians(desLongitude-desLongitude);
		
		Double a=Math.sin(dlat/2)*Math.sin(dlat/2)
				+Math.cos(Math.toRadians(srcLatitude))*Math.cos(Math.toRadians(desLatitude))
				+Math.sin(dlon/2)*Math.sin(dlon/2);
				;
				
		Double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		Double distance=EARTH_RADIUS*c;
		return distance;
	}
	
	public long calculateDuration(LocalDateTime startTime,LocalDateTime endTime) {
		Duration duration=Duration.between(startTime, endTime);
		
		return duration.getSeconds();
		
	}
	
	public Double calculateFare(Double distance) {
		Double baseFare=11.0;
		double totalFare=baseFare*distance;
		
		
		return totalFare;
		
	}

}
