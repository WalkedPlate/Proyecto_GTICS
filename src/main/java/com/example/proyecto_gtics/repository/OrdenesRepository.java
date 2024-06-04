package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.EstadoOrden;
import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.entity.Sedes;
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
    List<Ordenes> findByTipoOrdenAndSedesAndEstadoOrdenNot(TipoOrden tipoOrden, Sedes sedes, EstadoOrden estadoOrden);

    Ordenes findFirstByOrderByIdordenesDesc(); // recuperar la última entrada de la tabla

    @Query(value = "update ordenes set estado_orden_idestado_orden = ?1 where idordenes = ?2", nativeQuery = true)
    void cambiarEstadoOrden(Integer estadoOrden, Integer idOrden);

    @Query(value = "update ordenes set tipo_orden_idtipo_orden = ?1 where idordenes = ?2", nativeQuery = true)
    void cambiarTipoOrden(Integer tipoOrden, Integer idOrden);


    List<Ordenes> findByTipoOrden(Optional<TipoOrden> tipoOrden); //Buscar por tipo de orden

    List<Ordenes> findByTipoOrdenAndEstadoOrden(Optional<TipoOrden> tipoOrden , Optional<EstadoOrden> estadoOrden);

    @Query(value = "SELECT * FROM proyecto_gtics.ordenes where estado_orden_idestado_orden between ?1 and ?2 and tipo_orden_idtipo_orden = ?3",nativeQuery = true)
    List<Ordenes> encuentraOrdenesPorEstadoOrden(Integer ranMin, Integer ranMax,Integer idTipoOrden);

    Ordenes findByIdordenes(int idOrden);

    List<Ordenes> findByEstadoOrdenAndTipoOrdenOrTipoOrden(Optional<EstadoOrden> estadoOrden, Optional<TipoOrden> tipoOrden1 , Optional<TipoOrden> tipoOrden2);

    @Query(value = "SELECT * FROM proyecto_gtics.ordenes where estado_orden_idestado_orden between ?1 and ?2 \n" +
            "and (tipo_orden_idtipo_orden = ?3 or tipo_orden_idtipo_orden = ?4 or tipo_orden_idtipo_orden = ?5);",nativeQuery = true)
    List<Ordenes> encuentraOrdenesPorEstadosOrdenes(Integer ranMin, Integer ranMax,Integer idTipoOrden1 , Integer idTipoOrden2 , Integer idTipoOrden3 );


}
