package com.ride.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ride.entity.License;

public interface LicenseRepository extends JpaRepository<License, Integer>{

}
