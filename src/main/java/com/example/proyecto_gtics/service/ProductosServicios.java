package com.example.proyecto_gtics.service;

import com.example.proyecto_gtics.entity.Productos;
import com.example.proyecto_gtics.repository.ProductosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductosServicios {
    private final ProductosRepository productosRepository;
    @Autowired
    public ProductosServicios (ProductosRepository productosRepository){
        this.productosRepository = productosRepository;
    }
    public void crearProducto(Productos productos){
        this.productosRepository.save(productos);
    }


}
