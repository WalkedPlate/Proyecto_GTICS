package com.example.proyecto_gtics.dto;

public interface ProductoMejorVendido {
    Integer getIdProductos();
    String getNombre();
    String getDescripcion();
    float getPrecio();
    String getCategoria();

    Integer getIdCategorias();

    byte[] getFoto();
    Long getCantidadOrdenes();
}
