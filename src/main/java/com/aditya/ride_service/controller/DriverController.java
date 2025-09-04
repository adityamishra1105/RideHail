package com.aditya.ride_service.controller;


import com.aditya.ride_service.model.Driver;
import com.aditya.ride_service.repository.DriverRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
public class DriverController {
    private final DriverRepository driverRepo;

    public DriverController(DriverRepository driverRepo){
        this.driverRepo = driverRepo;
    }

    //update driver availability
    @PatchMapping("/{id}/availability")
    public ResponseEntity<?> updateAvailability(@PathVariable Long id, @RequestParam boolean available){
        Driver driver = driverRepo.findById(id).orElseThrow(() -> new RuntimeException("Driver Not Found"));
        driver.setAvailable(available);
        driverRepo.save(driver);

        return ResponseEntity.ok("Driver Availability Updated");
    }

    //update driver location
    @PatchMapping("{id}/location")
    public ResponseEntity<?> updateLocation(@PathVariable Long id, @RequestParam double lat, @RequestParam double lon){
        Driver driver = driverRepo.findById(id).orElseThrow(() -> new RuntimeException("Driver not found"));
        driver.setLatitude(lat);
        driver.setLongitude(lon);
        driverRepo.save(driver);

        return ResponseEntity.ok("Driver location updated");
    }
}
