package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.dto.CantProductPorOrdenClase;
import com.example.proyecto_gtics.dto.CantidadPorProductoSedeFecha;
import com.example.proyecto_gtics.dto.CantidadProductosPorOrden;
import com.example.proyecto_gtics.dto.NroTransaccionesPorSede;
import com.example.proyecto_gtics.entity.EstadoOrden;
import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.entity.Sedes;
import com.example.proyecto_gtics.entity.TipoOrden;
import com.example.proyecto_gtics.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenesRepository extends JpaRepository<Ordenes,Integer> {
    List<Ordenes> findByTipoOrdenAndUsuarios(TipoOrden tipoOrden, Usuarios usuarios);
    List<Ordenes> findByUsuarios(Usuarios usuarios);
    List<Ordenes> findByTipoOrdenAndSedesAndEstadoOrdenNot(TipoOrden tipoOrden, Sedes sedes, EstadoOrden estadoOrden);

    Ordenes findFirstByOrderByIdordenesDesc(); // recuperar la última entrada de la tabla

    @Query(value = "update ordenes set estado_orden_idestado_orden = ?1 where idordenes = ?2", nativeQuery = true)
    void cambiarEstadoOrden(Integer estadoOrden, Integer idOrden);

    @Query(value = "update ordenes set tipo_orden_idtipo_orden = ?1 where idordenes = ?2", nativeQuery = true)
    void cambiarTipoOrden(Integer tipoOrden, Integer idOrden);


    List<Ordenes> findByTipoOrden(Optional<TipoOrden> tipoOrden); //Buscar por tipo de orden

    List<Ordenes> findByTipoOrdenAndEstadoOrden(Optional<TipoOrden> tipoOrden , Optional<EstadoOrden> estadoOrden);

    @Query(value = "SELECT * FROM proyecto_gtics.ordenes where estado_orden_idestado_orden between ?1 and ?2 and tipo_orden_idtipo_orden = ?3 ",nativeQuery = true)
    List<Ordenes> encuentraOrdenesPorEstadoOrden(Integer ranMin, Integer ranMax,Integer idTipoOrden);

    Ordenes findByIdordenes(int idOrden);

    List<Ordenes> findByEstadoOrdenAndTipoOrdenOrTipoOrden(Optional<EstadoOrden> estadoOrden, Optional<TipoOrden> tipoOrden1 , Optional<TipoOrden> tipoOrden2);

    @Query(value = "SELECT * FROM proyecto_gtics.ordenes where estado_orden_idestado_orden between ?1 and ?2 \n" +
            "and (tipo_orden_idtipo_orden = ?3 or tipo_orden_idtipo_orden = ?4 or tipo_orden_idtipo_orden = ?5 or tipo_orden_idtipo_orden= ?6 ) and sedes_idsedes= ?7 ;",nativeQuery = true)
    List<Ordenes> encuentraOrdenesPorEstadosOrdenes(Integer ranMin, Integer ranMax,Integer idTipoOrden1 , Integer idTipoOrden2 , Integer idTipoOrden3, Integer idTipoOrden4,Integer idSede );

    @Query(value = "select coalesce(count(ordenes.sedes_idsedes),0) as nroTransacciones, sedes.nombre from sedes left join proyecto_gtics.ordenes on ordenes.sedes_idsedes= sedes.idsedes where (ordenes.tipo_orden_idtipo_orden between 1 and 4 or ordenes.tipo_orden_idtipo_orden is null) and (ordenes.estado_orden_idestado_orden between 4 and 10 or ordenes.estado_orden_idestado_orden is null) group by sedes.nombre;",nativeQuery = true)
    List<NroTransaccionesPorSede> encuentraNroTransaccinesPorSede();

    @Query(value="select sum(detalles_orden.cantidad) as cantidad, sedes.nombre as nombreSede,productos.nombre as medicamento from proyecto_gtics.ordenes inner join detalles_orden on ordenes.idordenes= detalles_orden.ordenes_idordenes inner join sedes on ordenes.sedes_idsedes=sedes.idsedes inner join productos on detalles_orden.productos_idproductos=productos.idproductos where (ordenes.tipo_orden_idtipo_orden=1 or ordenes.tipo_orden_idtipo_orden=3 or ordenes.tipo_orden_idtipo_orden=4) AND sedes.idsedes=?1 AND ordenes.fecha_registro >= NOW() - INTERVAL 3 month group by medicamento;",nativeQuery = true)
    List<CantidadPorProductoSedeFecha> cantidadPorSedePorProductoPorFechaMes(Integer idAdminSede);

    @Query(value="select sum(detalles_orden.cantidad) as cantidad, sedes.nombre as nombreSede,productos.nombre as medicamento from proyecto_gtics.ordenes inner join detalles_orden on ordenes.idordenes= detalles_orden.ordenes_idordenes inner join sedes on ordenes.sedes_idsedes=sedes.idsedes inner join productos on detalles_orden.productos_idproductos=productos.idproductos where (ordenes.tipo_orden_idtipo_orden=1 or ordenes.tipo_orden_idtipo_orden=3 or ordenes.tipo_orden_idtipo_orden=4) AND sedes.idsedes=?1 AND ordenes.fecha_registro >= NOW() - INTERVAL ?2 day group by medicamento;",nativeQuery = true)
    List<CantidadPorProductoSedeFecha> cantidadPorSedePorProductoPorFechaDia(Integer idAdminSede,Integer nroDias);

    @Query(value = "select ordenes.idordenes,ordenes.estado_orden_idestado_orden as estado_orden,ordenes.sedes_idsedes,detalles_orden.productos_idproductos,detalles_orden.cantidad \n" +
            "from proyecto_gtics.ordenes inner join detalles_orden on ordenes.idordenes = detalles_orden.ordenes_idordenes \n" +
            "where ordenes.tipo_orden_idtipo_orden = ?1 and ordenes.sedes_idsedes = ?2 and ordenes.idordenes = ?3 ;",nativeQuery = true)
    List<CantidadProductosPorOrden> obtenerCantidadPorTipoOrdenSedeOrden(Integer tipoOrden, Integer idSede, Integer idOrden);

    List<Ordenes> findTop2ByTipoOrdenAndSedesAndEstadoOrdenNotInOrderByIdordenesDesc(TipoOrden tipoOrden, Sedes sedes, List<EstadoOrden> list);

}
