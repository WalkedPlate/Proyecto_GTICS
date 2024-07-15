package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.repository.OrdenesRepository;
import com.example.proyecto_gtics.service.OpenAIService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class OpenAIController {
    @Autowired
    private OpenAIService openAIService;
    @Autowired
    private OrdenesRepository ordenesRepository;

    private List<String> uploadedFiles = new ArrayList<>();

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

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            Files.write(path, file.getBytes());
            uploadedFiles.add(path.toString());
            return ResponseEntity.ok("Archivo subido exitosamente: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo");
        }
    }


}

@Getter
@Setter
class ChatRequest {
    private String message;
    private String prompt;
}