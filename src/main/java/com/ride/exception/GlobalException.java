package com.ride.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalException {
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorDetail> customExceptionHandler(CustomException exception){
		return new ResponseEntity<ErrorDetail>(new ErrorDetail(exception.getMessage(),false),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DriverException.class)
	public ResponseEntity<ErrorDetail> driverExceptionHandler(DriverException exception){
		return new ResponseEntity<ErrorDetail>(new ErrorDetail(exception.getMessage(),false),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RideException.class)
	public ResponseEntity<ErrorDetail> rideExceptionHandler(RideException exception){
		return new ResponseEntity<ErrorDetail>(new ErrorDetail(exception.getMessage(),false),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorDetail> userExceptionHandler(UserException exception){
		return new ResponseEntity<ErrorDetail>(new ErrorDetail(exception.getMessage(),false),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetail> methodArgsExceptionHandler(MethodArgumentNotValidException exception){
		return new ResponseEntity<ErrorDetail>(new ErrorDetail(exception.getMessage(),false),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorDetail> constraintViolationExceptionHandler(ConstraintViolationException exception){
		return new ResponseEntity<ErrorDetail>(new ErrorDetail(exception.getMessage(),false),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetail> otherExceptionHandler(Exception exception){
		return new ResponseEntity<ErrorDetail>(new ErrorDetail(exception.getMessage(),false),HttpStatus.BAD_REQUEST);
	}

}
