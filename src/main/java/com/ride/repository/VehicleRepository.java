package com.ride.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ride.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

}
