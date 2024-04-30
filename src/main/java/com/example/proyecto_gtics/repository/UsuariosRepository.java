package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.EstadoUsuario;
import com.example.proyecto_gtics.entity.Sedes;
import com.example.proyecto_gtics.entity.TipoUsuario;
import com.example.proyecto_gtics.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios,Integer> {

    //Doctores
    List<Usuarios> findByTipoUsuarioAndSedesAndEstadoUsuario(TipoUsuario tipoUsuario, Sedes sedes, EstadoUsuario estadoUsuario);

    //Farmacistas
    List<Usuarios> findByTipoUsuarioAndEstadoUsuario(TipoUsuario tipoUsuario, EstadoUsuario estadoUsuario);

    Usuarios findByIdUsuario(int idUsuario);




}
