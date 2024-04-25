package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "productos_has_sedes")
public class ProductosSedes {
    @EmbeddedId
    private ProductosSedesId id;

    @MapsId("idProductos")
    @ManyToOne
    @JoinColumn(name = "productos_idproductos")
    private Productos productosId;

    @MapsId("idSedes")
    @ManyToOne
    @JoinColumn(name = "sedes_id_sedes")
    private Sedes sedesId;
}
