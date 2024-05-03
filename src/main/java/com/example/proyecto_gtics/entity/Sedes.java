package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sedes")
public class Sedes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsedes" , nullable = false)
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
