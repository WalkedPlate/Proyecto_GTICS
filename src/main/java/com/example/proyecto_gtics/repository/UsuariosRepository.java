package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.TipoUsuario;
import com.example.proyecto_gtics.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios,Integer> {

    List<Usuarios> findByTipoUsuario(TipoUsuario tipoUsuario);


}
