package com.ride.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride.entity.Driver;
import com.ride.entity.Ride;
import com.ride.service.DriverService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/driver")
@RequiredArgsConstructor
public class DriverController {

	private final DriverService driverService;

	@GetMapping("/profile")
	public ResponseEntity<Driver> getReqDriverProfile(@RequestHeader("Authorization") String jwt) {
		Driver driver = driverService.getDriverProfile(jwt);

		return new ResponseEntity<Driver>(driver, HttpStatus.OK);
	}

	@GetMapping("/{driverId}/current-ride")
	public ResponseEntity<Ride> getCurrentRide(@PathVariable Integer driverId) {
		Ride ride = driverService.getDriversCurrentRides(driverId);
		return new ResponseEntity<Ride>(ride, HttpStatus.OK);
	}

	@GetMapping("/rides/completed")
	public ResponseEntity<List<Ride>> getCompletedRides(@RequestHeader("Authorization") String jwt) {
		Driver driver = driverService.getDriverProfile(jwt);
		
		List<Ride> rides = driverService.completedRides(driver.getId());

		return new ResponseEntity<List<Ride>>(rides, HttpStatus.OK);
	}

	@GetMapping("/{driverId}/allocated")
	public ResponseEntity<List<Ride>> getAllocatedRides(@PathVariable Integer driverId) {
		List<Ride> rides = driverService.getAllAllocatedRides(driverId);
		return new ResponseEntity<List<Ride>>(rides, HttpStatus.OK);
	}

}
