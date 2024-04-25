package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "detalles_orden")
public class DetallesOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddetalles_orden" , nullable = false)
    private Integer idDetallesOrden;

    @ManyToOne
    @JoinColumn(name = "ordenes_idordenes",nullable = false)
    private Ordenes ordenes;

    @ManyToOne
    @JoinColumn(name = "productos_idproductos",nullable = false)
    private Productos productos;

    @Column(name = "cantidad",nullable = false)
    private Integer cantidad;

    @Column(name = "monto_parcial")
    private float montoParcial;

}
