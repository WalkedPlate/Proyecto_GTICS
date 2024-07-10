package com.example.proyecto_gtics.dto;

import com.example.proyecto_gtics.entity.Categorias;

public interface ProductosTendencia {
    Integer getIdProductos();
    String getNombre();
    String getDescripcion();
    float getPrecio();
    Categorias getCategorias();

    byte[] getFoto();
    Long getCantidadOrdenes();
}
