package com.example.proyecto_gtics.service;

import com.example.proyecto_gtics.entity.Productos;
import com.example.proyecto_gtics.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BuscarProductosService {
    @Autowired
    private ProductosRepository productosRepository;

    public Page<Productos> findPaginated(Pageable pageable) {
        return productosRepository.findAll(pageable);
    }
}
