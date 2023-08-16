package com.ride.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ride.entity.Ride;
import com.ride.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
	
	@Query("SELECT r FROM Ride r WHERE r.status=COMPLETED AND r.user.id=:userId")
	public List<Ride> getCompletedRides(@Param("userId") Integer userId);
}
