package com.example.proyecto_gtics.dto;

public interface ProductosAnadidosRecientemente {
    Integer getIdProductos();
    String getNombre();
    String getDescripcion();
    float getPrecio();
    String getCategoria();

    Integer getIdCategorias();

    byte[] getFoto();
    Long getCantidadOrdenes();
}
