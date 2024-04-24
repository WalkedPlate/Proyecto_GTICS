package com.example.proyecto_gtics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Embeddable
public class ProductosSedes implements Serializable {
    @Column(name = "productos_idproductos",nullable = false)
    private Integer idProductos;

    @Column(name = "sedes_idsedes",nullable = false)
    private Integer idSedes;
}
