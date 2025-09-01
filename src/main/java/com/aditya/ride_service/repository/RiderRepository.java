package com.aditya.ride_service.repository;

import com.aditya.ride_service.model.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {
}
