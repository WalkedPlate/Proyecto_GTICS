package com.example.proyecto_gtics.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GoogleMapsService {


    private String apiKey = "AIzaSyCXl59fxmrrijMF3f6ActbwkX4qLwOzdnE";



    public String getGeocodingData(String address) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("address", address)
                .queryParam("key", apiKey)
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }

}
