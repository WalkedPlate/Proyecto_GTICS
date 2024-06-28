package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private CardService cardService;

    @GetMapping("/test-validate")
    public boolean testValidate(@RequestParam String cardNumber,
                                @RequestParam String holderName,
                                @RequestParam String expirationDate,
                                @RequestParam String cvv) {
        return cardService.validateCreditCard(cardNumber, holderName, expirationDate, cvv);
    }
}