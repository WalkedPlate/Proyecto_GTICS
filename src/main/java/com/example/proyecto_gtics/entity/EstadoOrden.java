package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "estado_orden")
public class EstadoOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idestado_orden" , nullable = false)
    private Integer idEstadoOrden;

    @Column(name = "nombre",nullable = false,length = 80)
    private String nombre;
}
