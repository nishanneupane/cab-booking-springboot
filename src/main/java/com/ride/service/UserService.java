package com.ride.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ride.entity.Ride;
import com.ride.entity.User;
import com.ride.exception.UserException;
import com.ride.jwt.JwtService;
import com.ride.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final JwtService jwtService;
	
//	public User createUser(User user) throws UserException{
//		return null;
//	}
	
	public User getReqUserProfile(String token) throws UserException{
		String email = jwtService.getUsernameFromJwt(token);
		User user = userRepository.findByEmail(email).orElseThrow(()->new UserException("user not found with this token ."));
		
		return user;
	}
	
	public User findUserById(Integer userId) throws UserException{
		return userRepository.findById(userId).orElseThrow(()->new UserException("user not found with this token ."));
	}
	
	public User findUserByEmail(String email) throws UserException{
		return userRepository.findByEmail(email).orElseThrow(()->new UserException("user not found with this token ."));
	}
	
	public User findUserByToken(String token) throws UserException{
		String usernameFromJwt = jwtService.getUsernameFromJwt(token);
		User user = userRepository.findByEmail(usernameFromJwt).orElseThrow(()->new UserException("user not found with this token ."));
		return user;
	}
	
	public List<Ride> completedRides(Integer userId) throws UserException{
		
		return userRepository.getCompletedRides(userId);
	}
}
