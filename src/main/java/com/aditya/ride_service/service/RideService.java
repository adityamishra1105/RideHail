package com.aditya.ride_service.service;

import com.aditya.ride_service.model.*;
import com.aditya.ride_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RiderRepository riderRepo;
    private final DriverRepository driverRepo;
    private final RideRepository rideRepo;

    // Request a ride
    public Ride requestRide(Long riderId, String pickup, String dropOff) {
        Rider rider = riderRepo.findById(riderId)
                .orElseThrow(() -> new RuntimeException("Rider not found with ID: " + riderId));

        // Find nearest available driver (for now, just first available)
        Optional<Driver> driverOpt = driverRepo.findAll()
                .stream()
                .filter(Driver::isAvailable)
                .findFirst();

        Ride ride = new Ride();
        ride.setRider(rider);
        ride.setPickupLocation(pickup);
        ride.setDropOffLocation(dropOff);
        ride.setState(RideState.LOOKING_FOR_DRIVER);

        if (driverOpt.isPresent()) {
            Driver driver = driverOpt.get();
            driver.setAvailable(false);
            driverRepo.save(driver);

            ride.setDriver(driver);
            ride.setState(RideState.DRIVER_ASSIGNED);
        }

        return rideRepo.save(ride);
    }

    // Complete ride and make driver available again
    public Ride completeRide(Long rideId) {
        Ride ride = rideRepo.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with ID: " + rideId));

        ride.setState(RideState.COMPLETED);

        Driver driver = ride.getDriver();
        if (driver != null) {
            driver.setAvailable(true);
            driverRepo.save(driver);
        }

        return rideRepo.save(ride);
    }

    // Find nearest driver using Haversine distance
    public Driver findNearestDriver(double riderLat, double riderLon) {
        List<Driver> availableDrivers = driverRepo.findAll()
                .stream()
                .filter(Driver::isAvailable)
                .filter(d -> d.getLatitude() != null && d.getLongitude() != null)
                .toList();

        if (availableDrivers.isEmpty()) {
            throw new RuntimeException("No available drivers nearby");
        }

        return availableDrivers.stream()
                .min(Comparator.comparingDouble(d -> distance(riderLat, riderLon, d.getLatitude(), d.getLongitude())))
                .orElseThrow(() -> new RuntimeException("No driver found after filtering"));
    }

    // Haversine formula for distance calculation
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }
}
