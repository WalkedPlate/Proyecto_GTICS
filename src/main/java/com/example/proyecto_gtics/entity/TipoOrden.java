package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tipo_orden")
public class TipoOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_orden" , nullable = false)
    private Integer idTipoOrden;

    @Column(name = "nombre",nullable = false,length = 80)
    private String nombre;
}
