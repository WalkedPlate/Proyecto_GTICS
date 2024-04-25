package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.DetallesOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallesOrdenRepository extends JpaRepository<DetallesOrden,Integer> {
}
