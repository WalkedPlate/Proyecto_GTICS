package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.Categorias;
import com.example.proyecto_gtics.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductosRepository extends JpaRepository<Productos,Integer> {
    List<Productos> findByNombre(String nombre);
    List<Productos> findByCategorias(Categorias categorias);

    List<Productos> findByEstadoProducto(String estadoProducto);

    long countByCategorias(Categorias categorias);
}
