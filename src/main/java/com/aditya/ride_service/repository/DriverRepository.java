package com.aditya.ride_service.repository;

import com.aditya.ride_service.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DriverRepository extends JpaRepository<Driver, Long>{
}
