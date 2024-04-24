package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriasRepository extends JpaRepository<Categorias,Integer> {
}
