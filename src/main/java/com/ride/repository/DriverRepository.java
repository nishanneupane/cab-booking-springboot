package com.ride.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ride.entity.Driver;
import com.ride.entity.Ride;

public interface DriverRepository extends JpaRepository<Driver, Integer>{
	Optional<Driver> findByEmail(String email);

	@Query("SELECT r FROM Ride r WHERE r.status = 'REQUESTED' AND r.driver.id = :driverId")
	List<Ride> getAllocatedRides(@Param("driverId") Integer driverId);
	
	@Query("SELECT r FROM Ride r WHERE r.status = 'COMPLETED' AND r.driver.id = :driverId")
	List<Ride> getCompletedRides(@Param("driverId") Integer driverId);
}
