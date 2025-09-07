package com.aditya.ride_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverLocationService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String DRIVER_LOC_KEY = "driver-locations";

    // Add or update driver location in Redis
    public void updateDriverLocation(Long driverId, double latitude, double longitude) {
        redisTemplate.opsForGeo()
                .add(DRIVER_LOC_KEY, new RedisGeoCommands.GeoLocation<>(driverId.toString(),
                        new org.springframework.data.geo.Point(longitude, latitude)));
    }

    // Get nearest drivers within given radius (km)
    public List<RedisGeoCommands.GeoLocation<String>> findNearestDrivers(double latitude, double longitude, double radiusKm) {
        var results = redisTemplate.opsForGeo().radius(
                DRIVER_LOC_KEY,
                new org.springframework.data.geo.Circle(
                        new org.springframework.data.geo.Point(longitude, latitude),
                        new org.springframework.data.geo.Distance(radiusKm, org.springframework.data.geo.Metrics.KILOMETERS)
                )
        );

        return results != null ? results.getContent().stream()
                .map(GeoResult::getContent)
                .toList() : List.of();
    }
}
