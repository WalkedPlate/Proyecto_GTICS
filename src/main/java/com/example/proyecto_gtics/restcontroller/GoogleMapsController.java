package com.example.proyecto_gtics.restcontroller;

import com.example.proyecto_gtics.service.GoogleMapsService;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin
@RequestMapping("/maps")
public class GoogleMapsController {

    private final GoogleMapsService googleMapsService;
    private Map<Long, Ubicacion> ubicacionesPedidos = new ConcurrentHashMap<>();

    public GoogleMapsController(GoogleMapsService googleMapsService) {
        this.googleMapsService = googleMapsService;
    }

    //Obtener coordenas según una dirección
    @GetMapping("/geocode")
    public String getGeocode(@RequestParam String address) {
        return googleMapsService.getGeocodingData(address);
    }

    //Para actualizar una ubicación
    @PostMapping("/ubicacion")
    public ResponseEntity<String> actualizarUbicacion(@RequestParam Long ordenId, @RequestParam double latitud, @RequestParam double longitud) {
        ubicacionesPedidos.put(ordenId, new Ubicacion(latitud, longitud));
        return ResponseEntity.ok("Ubicación actualizada");
    }

    @GetMapping("/ubicacion/{ordenId}")
    public ResponseEntity<Ubicacion> obtenerUbicacion(@PathVariable Long ordenId) {
        Ubicacion ubicacion = ubicacionesPedidos.get(ordenId);
        if (ubicacion != null) {
            return ResponseEntity.ok(ubicacion);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



    @Getter
    private static class Ubicacion {
        private double latitud;
        private double longitud;

        public Ubicacion(double latitud, double longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }

    }



}
