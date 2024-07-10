package com.example.proyecto_gtics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardValidationRequest {
    private String holderName;
    private String cardNumber;
    private String expirationDate;
    private String cvv;


}
