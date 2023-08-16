package com.ride.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ride.dto.Calculator;
import com.ride.entity.Driver;
import com.ride.entity.License;
import com.ride.entity.Ride;
import com.ride.entity.RideStatus;
import com.ride.entity.UserRole;
import com.ride.entity.Vehicle;
import com.ride.exception.DriverException;
import com.ride.jwt.JwtService;
import com.ride.repository.DriverRepository;
import com.ride.repository.LicenseRepository;
import com.ride.repository.RideRepository;
import com.ride.repository.VehicleRepository;
import com.ride.request.DriverSignupRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService {

	private final DriverRepository driverRepository;
	private final PasswordEncoder passwordEncoder;
	private final VehicleRepository vehicleRepository;
	private final LicenseRepository licenseRepository;
	private final RideRepository rideRepository;
	private final JwtService jwtService;
	private final Calculator distanceCalculator;

	public Driver driverRegister(@RequestBody DriverSignupRequest req) {
		License license = req.getLicense();
		Vehicle vehicle = req.getVehicle();
		String encodedPassword = passwordEncoder.encode(req.getPassword());

		License newLicense = new License();
		newLicense.setLinceseState(license.getLinceseState());
		newLicense.setLicenseNumber(license.getLicenseNumber());
		newLicense.setLicenseExpirationDate(license.getLicenseExpirationDate());
		newLicense.setId(license.getId());

		License savedLicense = licenseRepository.save(newLicense);

		Driver driver = new Driver();
		driver.setEmail(req.getEmail());
		driver.setName(req.getName());
		driver.setPassword(encodedPassword);
		driver.setRole(UserRole.DRIVER);
		driver.setMobile(req.getMobile());
		driver.setLatitude(req.getLatitide());
		driver.setLongitude(req.getLongitude());
		driver.setLicense(savedLicense);
		driver.setVehicle(vehicle);

		Vehicle newVehicle = new Vehicle();
		newVehicle.setCapacity(vehicle.getCapacity());
		newVehicle.setColor(vehicle.getColor());
		newVehicle.setId(vehicle.getId());
		newVehicle.setLicensePlate(vehicle.getLicensePlate());
		newVehicle.setMake(vehicle.getMake());
		newVehicle.setModel(vehicle.getModel());
		newVehicle.setYear(vehicle.getYear());
		newVehicle.setDriver(driver);

		vehicleRepository.save(newVehicle);

		return driverRepository.save(driver);

	}

	public List<Driver> getAvailableDrivers(Double pickupLatitude, Double pickupLongitude, Ride ride) {
		List<Driver> drivers = driverRepository.findAll();

		List<Driver> availableDrivers = new ArrayList<>();

		for (Driver driver : drivers) {
			if (driver.getCurrentRide() != null && driver.getCurrentRide().getStatus() != RideStatus.COMPLETED) {
				continue;
			}
			if (ride.getDeclinedDrivers().contains(driver.getId())) {
				continue;
			}

			Double driverLatitude = driver.getLatitude();
			Double driverLongitude = driver.getLongitude();

			Double distance = distanceCalculator.calculateDistance(driverLatitude, driverLongitude, pickupLatitude,
					pickupLongitude);

			ride.setDistance(distance);
			availableDrivers.add(driver);
		}

		return availableDrivers;
	}

	public Driver findNearestDriver(List<Driver> availableDrivers, Double pickupLatitude, Double pickupLongitude) {

		Double min = Double.MAX_VALUE;
		Driver nearestDriver = null;
		

		for (Driver driver : availableDrivers) {
			Double driverLatitude = driver.getLatitude();
			Double driverLongitude = driver.getLongitude();
			Double distance = distanceCalculator.calculateDistance(driverLatitude, driverLongitude, pickupLatitude, pickupLongitude);
			
			if(min>distance) {
				min=distance;
				nearestDriver=driver;
			}
//			nearestDriver=driver;

		}


		return nearestDriver;
	}

	public Driver getDriverProfile(String jwt) throws DriverException {
		String email = jwtService.getUsernameFromJwt(jwt);
		
		Driver driver = driverRepository.findByEmail(email).orElseThrow(()->new DriverException("Driver doesnt exists with this email : "+email));
		
		return driver;
	}

	public Driver findByDriverId(Integer driverId) throws DriverException {
		return driverRepository.findById(driverId).orElseThrow(()->new DriverException("Driver doesnt exists with this id : "));
	}

	public List<Ride> completedRides(Integer driverId) throws DriverException {
		return driverRepository.getCompletedRides(driverId);
//		return rideRepository.findAll();
	}

	public List<Ride> getAllAllocatedRides(Integer driverId) throws DriverException {
//		return driverRepository.getAllocatedRides(driverId);
		return rideRepository.findAll();
	}

	public Ride getDriversCurrentRides(Integer driverId) throws DriverException {
		Driver driver = driverRepository.findById(driverId).orElseThrow(()->new DriverException("Driver doesnt exists with this id : "));
		
		return driver.getCurrentRide();
	}

}
