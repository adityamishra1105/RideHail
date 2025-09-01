package com.aditya.ride_service.repository;

import com.aditya.ride_service.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RideRepository extends JpaRepository<Ride, Long>{
}
