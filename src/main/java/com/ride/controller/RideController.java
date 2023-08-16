package com.ride.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride.dto.MessageResponse;
import com.ride.dto.RideDto;
import com.ride.entity.Driver;
import com.ride.entity.Ride;
import com.ride.entity.User;
import com.ride.exception.RideException;
import com.ride.exception.UserException;
import com.ride.request.RideRequest;
import com.ride.request.StartRideRequest;
import com.ride.service.DriverService;
import com.ride.service.RideService;
import com.ride.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ride")
@RequiredArgsConstructor
public class RideController {
	private final DriverService driverService;
	private final RideService rideService;
	private final UserService userService;
	private final ModelMapper modelMapper;

	@PostMapping("/request")
	public ResponseEntity<RideDto> userRequestRideHandler(@RequestBody RideRequest rideRequest,
			@RequestHeader("Authorization") String jwt) {
		User user = userService.getReqUserProfile(jwt);
		Ride ride = rideService.requestRide(rideRequest, user);

		return new ResponseEntity<RideDto>(modelMapper.map(ride, RideDto.class), HttpStatus.OK);
	}

	@PutMapping("/{rideId}/accept")
	public ResponseEntity<MessageResponse> acceptRideHandler(@PathVariable Integer rideId)
			throws UserException, RideException {

		rideService.acceptRide(rideId);
		
		MessageResponse messageResponse=new MessageResponse("Ride is accepted by Driver . ");
		
		return new ResponseEntity<MessageResponse>(messageResponse,HttpStatus.ACCEPTED);

	}
	
	@PutMapping("/{rideId}/decline")
	public ResponseEntity<MessageResponse> declineRideHandler(@RequestHeader("Authorization") String jwt ,@PathVariable Integer rideId)
			throws UserException, RideException {

		Driver profile = driverService.getDriverProfile(jwt);
		rideService.declineRide(rideId, profile.getId());
		
		MessageResponse messageResponse=new MessageResponse("Ride is cancelled by Driver . ");
		
		return new ResponseEntity<MessageResponse>(messageResponse,HttpStatus.OK);

	}
	
	@PutMapping("/{rideId}/start")
	public ResponseEntity<MessageResponse> startRideHandler(@PathVariable Integer rideId,@RequestBody StartRideRequest request)throws UserException, RideException {

		rideService.startRide(rideId, request.getOtp());
		
		MessageResponse messageResponse=new MessageResponse("Ride is Started . ");
		
		return new ResponseEntity<MessageResponse>(messageResponse,HttpStatus.OK);

	}
	
	@PutMapping("/{rideId}/complete")
	public ResponseEntity<MessageResponse> completeRideHandler(@PathVariable Integer rideId)throws UserException, RideException {

		rideService.completeRide(rideId);
		
		MessageResponse messageResponse=new MessageResponse("Ride is completed sucessfully :) . ");
		
		return new ResponseEntity<MessageResponse>(messageResponse,HttpStatus.OK);

	}
	
	@GetMapping("/{rideId}")
	public ResponseEntity<RideDto> findRideById(@PathVariable Integer rideId,@RequestHeader("Authorization") String jwt) throws UserException, RideException{
		
		User user = userService.findUserByToken(jwt);
		Ride ride = rideService.findRideById(rideId);
		
		
		return new ResponseEntity<RideDto>(modelMapper.map(ride, RideDto.class),HttpStatus.OK);
	}

}
