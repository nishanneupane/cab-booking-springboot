package com.ride.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride.entity.Ride;
import com.ride.entity.User;
import com.ride.exception.UserException;
import com.ride.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/{userId}")
	public ResponseEntity<User> findUserById(@PathVariable Integer userId) {
		return new ResponseEntity<User>(userService.findUserById(userId),HttpStatus.OK);
	}

	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) {
		return new ResponseEntity<User>(userService.getReqUserProfile(jwt),HttpStatus.OK);
	}
	
	
	@GetMapping("/rides/completed")
	public ResponseEntity<List<Ride>> getCompletedRideHandler(@RequestHeader("Authorization") String jwt) throws UserException{
		User user = userService.getReqUserProfile(jwt);
		List<Ride> completedRides = userService.completedRides(user.getId());
		
		return new ResponseEntity<List<Ride>>(completedRides,HttpStatus.OK);
	}

}
