package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.EstadoOrden;
import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.entity.TipoOrden;
import com.example.proyecto_gtics.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenesRepository extends JpaRepository<Ordenes,Integer> {
    List<Ordenes> findByTipoOrdenAndUsuarios(TipoOrden tipoOrden, Usuarios usuarios);
    Ordenes findFirstByOrderByIdordenesDesc(); // recuperar la última entrada de la tabla

    @Query(value = "update ordenes set estado_orden_idestado_orden = ?1 where idrdenes = ?2", nativeQuery = true)
    void cambiarEstadoOrden(Integer estadoOrden, Integer idOrden);

    List<Ordenes> findByTipoOrden(Optional<TipoOrden> tipoOrden); //Buscar por tipo de orden

    List<Ordenes> findByTipoOrdenAndEstadoOrden(Optional<TipoOrden> tipoOrden , Optional<EstadoOrden> estadoOrden);

    
}
