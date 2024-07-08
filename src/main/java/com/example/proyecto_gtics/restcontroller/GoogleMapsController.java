package com.example.proyecto_gtics.restcontroller;

import com.example.proyecto_gtics.service.GoogleMapsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleMapsController {

    private final GoogleMapsService googleMapsService;

    public GoogleMapsController(GoogleMapsService googleMapsService) {
        this.googleMapsService = googleMapsService;
    }

    @GetMapping("/geocode")
    public String getGeocode(@RequestParam String address) {
        return googleMapsService.getGeocodingData(address);
    }
}
