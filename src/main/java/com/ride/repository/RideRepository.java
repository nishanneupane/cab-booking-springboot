package com.ride.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ride.entity.Ride;

public interface RideRepository extends JpaRepository< Ride, Integer>{

}
