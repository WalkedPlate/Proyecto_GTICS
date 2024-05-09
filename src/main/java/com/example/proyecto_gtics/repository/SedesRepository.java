package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.Sedes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SedesRepository extends JpaRepository<Sedes,Integer> {
    Sedes findByIdSedes(Integer idSede);
}
