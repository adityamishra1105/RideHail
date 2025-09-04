// This is the APIs file

package com.aditya.ride_service.controller;

import com.aditya.ride_service.model.Driver;
import com.aditya.ride_service.model.Ride;
import com.aditya.ride_service.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    // Request a ride
    @PostMapping("/request")
    public ResponseEntity<Ride> requestRide(@RequestParam Long riderId,
                                            @RequestParam String pickup,
                                            @RequestParam String dropOff) {
        Ride ride = rideService.requestRide(riderId, pickup, dropOff);
        return ResponseEntity.ok(ride);
    }

    // Complete ride
    @PostMapping("/{rideId}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable Long rideId) {
        Ride ride = rideService.completeRide(rideId);
        return ResponseEntity.ok(ride);
    }

    // Find nearest driver (requires latitude & longitude)
    @GetMapping("/nearest-driver")
    public ResponseEntity<Driver> findNearestDriver(@RequestParam double lat,
                                                    @RequestParam double lon) {
        Driver driver = rideService.findNearestDriver(lat, lon);
        return ResponseEntity.ok(driver);
    }
}
