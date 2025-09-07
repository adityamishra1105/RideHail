package com.aditya.ride_service.controller;

import com.aditya.ride_service.model.Driver;
import com.aditya.ride_service.repository.DriverRepository;
import com.aditya.ride_service.service.DriverLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor // This auto-generates a constructor for final fields
public class DriverController {

    private final DriverRepository driverRepo;
    private final DriverLocationService driverLocationService; // Injected properly

    // Update driver availability
    @PatchMapping("/{id}/availability")
    public ResponseEntity<String> updateAvailability(@PathVariable Long id,
                                                     @RequestParam boolean available) {
        Driver driver = driverRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver Not Found"));
        driver.setAvailable(available);
        driverRepo.save(driver);
        return ResponseEntity.ok("Driver Availability Updated");
    }

    // Update driver location in DB + Redis
    @PatchMapping("/{id}/location")
    public ResponseEntity<String> updateLocation(@PathVariable Long id,
                                                 @RequestParam double lat,
                                                 @RequestParam double lon) {
        Driver driver = driverRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        driver.setLatitude(lat);
        driver.setLongitude(lon);
        driverRepo.save(driver);

        // Update in Redis too
        driverLocationService.updateDriverLocation(id, lat, lon);

        return ResponseEntity.ok("Driver location updated in DB and Redis!");
    }

    // Register new driver
    @PostMapping
    public ResponseEntity<Driver> registerDriver(@RequestBody Driver driver) {
        Driver saved = driverRepo.save(driver);
        return ResponseEntity.ok(saved);
    }

    // Get all drivers (debugging)
    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverRepo.findAll();
    }
}
