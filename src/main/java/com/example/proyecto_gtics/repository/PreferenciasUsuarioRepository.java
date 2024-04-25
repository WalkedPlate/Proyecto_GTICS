package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.PreferenciasUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenciasUsuarioRepository extends JpaRepository<PreferenciasUsuario,Integer> {
}
