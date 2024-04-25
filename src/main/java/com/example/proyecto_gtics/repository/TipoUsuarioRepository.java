package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario,String> {
}
