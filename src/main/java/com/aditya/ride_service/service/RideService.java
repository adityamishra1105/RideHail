package com.aditya.ride_service.service;

import com.aditya.ride_service.model.*;
import com.aditya.ride_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideService {
    private final RiderRepository riderRepo;
    private final DriverRepository driverRepo;
    private final RideRepository rideRepo;

    public Ride requestRide(Long riderId, String pickup, String dropOff){
        Rider rider = riderRepo.findById(riderId).orElseThrow();

        // for now: assign first available driver
        Optional<Driver> driverOpt = driverRepo.findAll().stream().filter(Driver::isAvailable).findFirst();

        Ride ride = new Ride();
        ride.setRider(rider);
        ride.setPickupLocation(pickup);
        ride.setDropOffLocation(dropOff);
        ride.setState(RideState.LOOKING_FOR_DRIVER);

        if (driverOpt.isPresent()){
            Driver driver = driverOpt.get();
            driver.setAvailable(false);
            driverRepo.save(driver);

            ride.setDriver(driver);
            ride.setState(RideState.DRIVER_ASSIGNED);
        }
        return rideRepo.save(ride);
    }

    public Ride completeRide(Long rideId){
        Ride ride = rideRepo.findById(rideId).orElseThrow();
        ride.setState((RideState.COMPLETED));
        ride.getDriver().setAvailable(true);
        driverRepo.save(ride.getDriver());
        return rideRepo.save(ride);
    }

}
