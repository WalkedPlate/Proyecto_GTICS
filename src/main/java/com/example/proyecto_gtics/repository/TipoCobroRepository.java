package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.TipoCobro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoCobroRepository extends JpaRepository<TipoCobro,Integer> {
}
