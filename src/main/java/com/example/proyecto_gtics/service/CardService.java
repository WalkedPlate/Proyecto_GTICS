package com.example.proyecto_gtics.service;
import com.example.proyecto_gtics.dto.CardValidationRequest;
import com.example.proyecto_gtics.entity.CreditCard;
import com.example.proyecto_gtics.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class CardService {


    @Autowired
    private CardRepository cardRepository;

    public boolean validateCard(CardValidationRequest request) {
        CreditCard card = cardRepository.findByCardNumberAndHolderNameAndExpirationDateAndCvv(
                request.getCardNumber(),
                request.getHolderName(),
                request.getExpirationDate(),
                request.getCvv()
        );
        return card != null;
    }

    /*
    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validateCreditCard(String cardNumber, String holderName, String expirationDate, String cvv) {
        String url = String.format("http://localhost:8081/api/creditcards/validate?cardNumber=%s&holderName=%s&expirationDate=%s&cvv=%s",
                cardNumber, holderName, expirationDate, cvv);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Boolean.class);
            return response.getBody() != null ? response.getBody() : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/
}
