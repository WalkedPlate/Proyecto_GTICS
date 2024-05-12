package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.CodigoColegio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodigoColegioRespository extends JpaRepository<CodigoColegio,Integer> {

    Optional<CodigoColegio> findByCodigo(String codigo);
}
