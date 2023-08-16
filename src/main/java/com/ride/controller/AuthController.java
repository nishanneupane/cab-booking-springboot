package com.ride.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride.dto.JwtResponse;
import com.ride.entity.Driver;
import com.ride.entity.User;
import com.ride.entity.UserRole;
import com.ride.exception.CustomException;
import com.ride.jwt.JwtService;
import com.ride.repository.DriverRepository;
import com.ride.repository.UserRepository;
import com.ride.request.DriverSignupRequest;
import com.ride.request.LoginRequest;
import com.ride.request.SignupRequest;
import com.ride.service.CustomUserDetailService;
import com.ride.service.DriverService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private final UserRepository userRepository;
	private final DriverRepository driverRepository;
	private final PasswordEncoder encoder;
	private final JwtService jwtService;
	private final CustomUserDetailService customUserDetailService;
	private final DriverService driverService;
	
	@PostMapping("/signup/user")
	public ResponseEntity<JwtResponse> signup(@RequestBody SignupRequest req){
		
		Optional<User> opt = userRepository.findByEmail(req.getEmail());
		if(!opt.isEmpty()) {
			throw new CustomException("user already exists");
		}
		
		String encodedPassword=encoder.encode(req.getPassword());
		
		User user=new User();
		user.setEmail(req.getEmail());
		user.setPassword(encodedPassword);
		user.setFullName(req.getFullName());
		user.setMobile(req.getMobile());
		user.setRole(UserRole.USER);
		
		User savedUser = userRepository.save(user);
		
		Authentication authentication=new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt=jwtService.generateToken(authentication);
		JwtResponse jwtResponse=new JwtResponse();
		jwtResponse.setJwt(jwt);
		jwtResponse.setAuthenticated(true);
		jwtResponse.setError(false);
		jwtResponse.setErrorDetails(null);
		jwtResponse.setRole(UserRole.USER);
		jwtResponse.setMessage("Acount created sucesfully ... : welcome : "+req.getFullName());
		return new ResponseEntity<JwtResponse>(jwtResponse,HttpStatus.CREATED);
	}
	
	@PostMapping("/signup/driver")
	public ResponseEntity<JwtResponse> signupForDriver(@RequestBody DriverSignupRequest req){
		
		Optional<User> opt = userRepository.findByEmail(req.getEmail());
		JwtResponse response=new JwtResponse();
		if(!opt.isEmpty()) {
			response.setError(true);
			response.setMessage("driver already exists with this email ");
			return new ResponseEntity<JwtResponse>(response,HttpStatus.BAD_REQUEST);
		}
		
		
		
		Driver savedDriver = driverService.driverRegister(req);		
		Authentication authentication=new UsernamePasswordAuthenticationToken(savedDriver.getEmail(), savedDriver.getPassword());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt=jwtService.generateToken(authentication);
		JwtResponse jwtResponse=new JwtResponse();
		jwtResponse.setJwt(jwt);
		jwtResponse.setAuthenticated(true);
		jwtResponse.setError(false);
		jwtResponse.setErrorDetails(null);
		jwtResponse.setRole(UserRole.DRIVER);
		jwtResponse.setMessage("Acount created sucesfully ... : welcome : "+req.getName());
		return new ResponseEntity<JwtResponse>(jwtResponse,HttpStatus.CREATED);
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req){
//		System.out.println(req.getEmail()+" " + req.getPassword());
		
		Authentication authentication2 = authenticate(req.getEmail(), req.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication2);

		
		String jwt=jwtService.generateToken(authentication2);
		JwtResponse jwtResponse=new JwtResponse();
		jwtResponse.setJwt(jwt);
		jwtResponse.setAuthenticated(true);
		jwtResponse.setError(false);
		jwtResponse.setErrorDetails(null);
		jwtResponse.setRole(UserRole.USER);
		jwtResponse.setMessage("Logged in sucessfully  ... : welcome : "+req.getEmail());
		return new ResponseEntity<JwtResponse>(jwtResponse,HttpStatus.OK);
	}
	
	private Authentication authenticate(String username,String password) {
		UserDetails details=customUserDetailService.loadUserByUsername(username);
		
		if(details==null) {
			throw new BadCredentialsException("Invalid Username or password . .  .");
		}
		if(!encoder.matches(password, details.getPassword())) {
			throw new BadCredentialsException("Invalid username or password  ....");
		}
		
		return new UsernamePasswordAuthenticationToken(details,null, details.getAuthorities());
	}
	
	

}
