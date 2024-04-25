package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoUsuarioRepository extends JpaRepository<EstadoUsuario,String> {
}
