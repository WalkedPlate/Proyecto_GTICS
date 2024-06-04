package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.DetallesOrden;
import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallesOrdenRepository extends JpaRepository<DetallesOrden,Integer> {

    List<DetallesOrden> findByOrdenes(Ordenes ordenReposicion);

    List<DetallesOrden> findByOrdenesIdordenes(Integer idOrden);

    @Query(value = "SELECT sum(monto_parcial) FROM proyecto_gtics.detalles_orden where ordenes_idordenes = ?1", nativeQuery = true)
    Float calcularMontoTotal(Integer idOrden);

    /*@Query(value = "select detalles_orden.cantidad, ordenes.tipo_orden_idtipo_orden as id_tipo_orden, ordenes_idordenes as id_orden ,productos.nombre,productos.foto,productos.codigo \n" +
            "from detalles_orden \n" +
            "inner join ordenes on detalles_orden.ordenes_idordenes = ordenes.idordenes and ordenes.tipo_orden_idtipo_orden=?1 \n" +
            "inner join productos on detalles_orden.productos_idproductos=productos.idproductos;",nativeQuery = true)
    List<DetallesOrdenPorOrdenDto> obtenerDetallesOrdenesPorTipoOrden(int idTipoOrden);*/


    @Query(value = "SELECT count(iddetalles_orden) FROM proyecto_gtics.detalles_orden where ordenes_idordenes = ?1", nativeQuery = true)
    Integer calcularCantidadDeDetallesPorOrden(Integer idOrden);
}
