package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.ProductosSedes;
import com.example.proyecto_gtics.entity.ProductosSedesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductosSedeRepository extends JpaRepository<ProductosSedes,ProductosSedesId> {
}
