package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "credit_card")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" , nullable = false)
    private Integer id;

    @Column(name = "card_number",length = 100,nullable = false)
    @NotBlank
    private String cardNumber;

    @Column(name = "holder_name",length = 100,nullable = false)
    @NotBlank
    private String holderName;

    @Column(name = "expiration_date",length = 100,nullable = false)
    @NotBlank
    private String expirationDate;

    @Column(name = "ccv",length = 100,nullable = false)
    @NotBlank
    private String cvv;
}
