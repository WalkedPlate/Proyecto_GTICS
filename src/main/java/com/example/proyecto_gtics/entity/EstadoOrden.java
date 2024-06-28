package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "estado_orden")
public class EstadoOrden implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idestado_orden" , nullable = false)
    private Integer idEstadoOrden;

    @Column(name = "nombre",nullable = false,length = 80)
    private String nombre;
}
