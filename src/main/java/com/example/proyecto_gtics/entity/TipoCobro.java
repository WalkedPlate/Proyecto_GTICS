package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tipo_cobro")
public class TipoCobro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_cobro" , nullable = false)
    private Integer idTipoCobro;

    @Column(name = "nombre",nullable = false,length = 45)
    private String nombre;
}
