package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.repository.OrdenesRepository;
import com.example.proyecto_gtics.service.OpenAIService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class OpenAIController {
    @Autowired
    private OpenAIService openAIService;
    @Autowired
    private OrdenesRepository ordenesRepository;

    @PostMapping("/gpt")
    public String chat(@RequestBody ChatRequest request) {
        try {
            return openAIService.getChatResponse(request.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }


    @PostMapping("/orden")
    public ResponseEntity<String> guardarOrden(@RequestBody Ordenes orden) {
        try {
            ordenesRepository.save(orden);
            return ResponseEntity.ok("Orden generada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar la orden: " + e.getMessage());
        }
    }

}

@Getter
@Setter
class ChatRequest {
    private String message;
    private String prompt;
}