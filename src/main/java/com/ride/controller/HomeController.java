package com.ride.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride.dto.MessageResponse;

@RestController
public class HomeController {
	
	@GetMapping("/")
	public ResponseEntity<MessageResponse> welcomeMenu(){
		return new ResponseEntity<MessageResponse>(new MessageResponse("welcome to our online cab booking application . \n Enjoy Your day"),HttpStatus.OK);
	}

}
