package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CreditCard, Integer> {
    CreditCard findByCardNumberAndHolderNameAndExpirationDateAndCvv(String cardNumber, String holderName, String expirationDate, String cvv);
}
