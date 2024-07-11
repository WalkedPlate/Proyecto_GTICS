package com.example.proyecto_gtics.service;

import com.example.proyecto_gtics.entity.Productos;
import com.example.proyecto_gtics.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductoBuscarServicio {

    @Autowired
    private ProductosRepository productosRepository;

    public List<Productos> searchProductos(String nombre) {
        return productosRepository.searchByKeyword(nombre);
    }
}
