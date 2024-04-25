package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.Ordenes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenesRepository extends JpaRepository<Ordenes,Integer> {
}
