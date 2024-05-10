package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "productos_has_sedes")
public class ProductosSedes {
    @EmbeddedId
    private ProductosSedesId id;

    @MapsId("idProductos")
    @ManyToOne
    @JoinColumn(name = "productos_idproductos")
    private Productos productos;

    @MapsId("idSedes")
    @ManyToOne
    @JoinColumn(name = "sedes_idsedes")
    private Sedes sedes;

    @Column(name = "cantidad" , nullable = false)
    private Integer cantidad;
}
