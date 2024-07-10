package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.dto.CardValidationRequest;
import com.example.proyecto_gtics.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;

    @PostMapping("/validate-card")
    public ResponseEntity<?> validateCard(@RequestBody CardValidationRequest request) {
        boolean isValid = cardService.validateCard(request);
        return ResponseEntity.ok(Collections.singletonMap("valid", isValid));
    }
}
