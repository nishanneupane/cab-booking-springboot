package com.ride.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.ride.dto.Calculator;
import com.ride.entity.Driver;
import com.ride.entity.Ride;
import com.ride.entity.RideStatus;
import com.ride.entity.User;
import com.ride.exception.RideException;
import com.ride.repository.DriverRepository;
import com.ride.repository.RideRepository;
import com.ride.request.RideRequest;

import ch.qos.logback.core.util.Duration;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RideService {

	private final RideRepository rideRepository;
	private final DriverRepository driverRepository;
	private final DriverService driverService;
	private final Calculator calculator;
//	private final NotificationR

	public Ride requestRide(RideRequest request, User user) throws RideException {
		Ride existingRide = new Ride();
		List<Driver> availableDrivers = driverService.getAvailableDrivers(request.getPickupLatitude(),
				request.getPickupLongitude(), existingRide);

		Driver findNearestDriver = driverService.findNearestDriver(availableDrivers, request.getPickupLatitude(),
				request.getPickupLongitude());
		if (findNearestDriver == null) {
			throw new RideException("driver not found near to you . :(");
		}

		Ride ride = createRide(user, findNearestDriver, request.getPickupLatitude(), request.getPickupLongitude(),
				request.getDestinationLatitude(), request.getDestinationLongitude(), request.getPickupArea(),
				request.getDestinationArea());

		return ride;
	}

	public Ride createRide(User user, Driver nearestDriver, Double pickupLatitude, Double pickupLongitude,
			Double desLatitude, Double desLongitude, String pickupArea, String desArea) throws RideException {

		Ride ride = new Ride();
		ride.setUser(user);
		ride.setDriver(nearestDriver);
		ride.setPickupLatitude(pickupLatitude);
		ride.setPickupLongitude(pickupLongitude);
		ride.setDestinationLatutude(desLatitude);
		ride.setDestinationLongitude(desLongitude);
		ride.setDestinatinArea(desArea);
		ride.setPickupArea(pickupArea);
		ride.setStatus(RideStatus.REQUESTED);

		return rideRepository.save(ride);
	}


	public void acceptRide(Integer rideId) throws RideException {
		Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RideException("ride not found with this id"));
		ride.setStatus(RideStatus.ACCEPTED);

		Driver driver = ride.getDriver();
		driver.setCurrentRide(ride);

		Random random = new Random();
		int otp = random.nextInt(9000) + 1000;
		ride.setOtp(otp);

		driverRepository.save(driver);
		rideRepository.save(ride);
	}

	public void declineRide(Integer rideId, Integer driverId) throws RideException {
		Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RideException("ride not found with this id"));

		ride.getDeclinedDrivers().add(driverId);

		List<Driver> availableDrivers = driverService.getAvailableDrivers(ride.getPickupLatitude(),
				ride.getPickupLongitude(), ride);

		Driver findNearestDriver = driverService.findNearestDriver(availableDrivers, ride.getPickupLatitude(),
				ride.getPickupLongitude());
		if (findNearestDriver == null) {
			throw new RideException("driver not found near to you . :(");
		}
		ride.setDriver(findNearestDriver);

		rideRepository.save(ride);
	}

	public void startRide(Integer rideId, Integer otp) throws RideException {
		Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RideException("ride not found with this id"));
		if (otp != ride.getOtp()) {
			throw new RideException("provided otp is incorrect ! :(");
		}
		ride.setStatus(RideStatus.STARTED);
		ride.setStartedTime(LocalDateTime.now());

		rideRepository.save(ride);

	}

	public void completeRide(Integer rideId) throws RideException {
		Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RideException("ride not found with this id"));

		ride.setStatus(RideStatus.COMPLETED);
		ride.setEndTime(LocalDateTime.now());

		Double distance = calculator.calculateDistance(ride.getDestinationLatutude(), ride.getDestinationLongitude(),
				ride.getPickupLatitude(), ride.getPickupLongitude());

		LocalDateTime startedTime = ride.getStartedTime();
		LocalDateTime endTime = ride.getEndTime();
		java.time.Duration duration = java.time.Duration.between(startedTime, endTime);
		long millis = duration.toMillis();

		Double fare = calculator.calculateFare(distance);
		ride.setFare((double) Math.round(fare));
		ride.setEndTime(LocalDateTime.now());

		Driver driver = ride.getDriver();

		driver.getRides().add(ride);
		driver.setCurrentRide(null);

		Integer driverRevenue = (int) ((driver.getTotalRevenue()==null)?(0 + Math.round(fare * 0.8)):(driver.getTotalRevenue()+Math.round(fare * 0.8)));
		driver.setTotalRevenue(driverRevenue);
		ride.setDistance(distance);
		ride.setDuration(millis);
		
		driverRepository.save(driver);
		rideRepository.save(ride);
	}

	public void cancelRide(Integer rideId) throws RideException {
		Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RideException("ride not found with this id"));

		ride.setStatus(RideStatus.CANCELLED);

		rideRepository.save(ride);

	}

	public Ride findRideById(Integer rideId) throws RideException {
		Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RideException("ride not found with this id"));

		return ride;
	}
}
