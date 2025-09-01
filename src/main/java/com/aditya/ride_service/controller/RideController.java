// This is the APIs file

package com.aditya.ride_service.controller;

import com.aditya.ride_service.model.Ride;
import com.aditya.ride_service.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {
    private final RideService rideService;

    @PostMapping("/request")
    public Ride requestRide(@RequestParam Long riderId, @RequestParam String pickup, @RequestParam String dropoff){
        return rideService.requestRide(riderId, pickup, dropoff);
    }

    @PostMapping("/{rideId}/complete")
    public Ride completeRide(@PathVariable Long rideId){
        return rideService.completeRide(rideId);
    }

}
