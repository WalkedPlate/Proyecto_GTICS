package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.Distritos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistritosRepository extends JpaRepository<Distritos,Integer> {

    Distritos findByNombre(String nombre);
}
