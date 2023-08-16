package com.ride.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ride.entity.Driver;
import com.ride.entity.User;
import com.ride.exception.CustomException;
import com.ride.exception.UserException;
import com.ride.repository.DriverRepository;
import com.ride.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final DriverRepository driverRepository;
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		List<GrantedAuthority> auths = new ArrayList<>();

		Optional<User> optionalUser = userRepository.findByEmail(username);
		Optional<Driver> optionalDriver = driverRepository.findByEmail(username);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), auths);
		}

		if (optionalDriver.isPresent()) {
			Driver driver = optionalDriver.get();
			return new org.springframework.security.core.userdetails.User(driver.getEmail(), driver.getPassword(),
					auths);
		}

		throw new UsernameNotFoundException("No user or driver found with the username: " + username);
	}
}

