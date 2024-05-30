package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "sedes")
public class Sedes implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsedes" , nullable = false)
    @Digits(integer = 10,fraction = 0)
    @Positive
    @Range(min = 1, max = 10, message = "no seas vivo")
    private Integer idSedes;

    @Column(name = "nombre",nullable = false,length = 45)
    private String nombre;

    @Column(name = "direccion",nullable = false,length = 45)
    private String direccion;

    @Column(name = "foto")
    private String foto;

    @Column(name = "asignado")
    private int asignado;
}
