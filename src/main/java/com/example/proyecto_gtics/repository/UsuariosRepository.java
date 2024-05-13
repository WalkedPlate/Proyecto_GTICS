package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios,Integer> {

    //Doctores
    List<Usuarios> findByTipoUsuarioAndSedesAndEstadoUsuario(TipoUsuario tipoUsuario, Sedes sedes, EstadoUsuario estadoUsuario);

    //Farmacistas
    List<Usuarios> findByTipoUsuarioAndEstadoUsuario(TipoUsuario tipoUsuario, EstadoUsuario estadoUsuario);

    Usuarios findByIdUsuario(int idUsuario);

    List<Usuarios> findByTipoUsuarioAndEstadoUsuarioOrEstadoUsuario(TipoUsuario tipoUsuario,EstadoUsuario estadoUsuario1,EstadoUsuario estadoUsuario2);

    Optional<Usuarios> findByDni(Integer dni);

    Usuarios findFirstByOrderByIdUsuarioDesc(); // recuperar la última entrada de la tabla

    Usuarios findByCorreo(String correo);


}
