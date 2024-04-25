package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.entity.TipoOrden;
import com.example.proyecto_gtics.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenesRepository extends JpaRepository<Ordenes,Integer> {
    List<Ordenes> findByTipoOrdenAndUsuarios(TipoOrden tipoOrden, Usuarios usuarios);
}
