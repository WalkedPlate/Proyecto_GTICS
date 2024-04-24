package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductosRepository extends JpaRepository<Productos,Integer> {
}
