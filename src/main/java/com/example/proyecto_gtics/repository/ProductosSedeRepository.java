package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.dto.CantidadProductosPorOrden;
import com.example.proyecto_gtics.dto.CantidadTotalPorProducto;
import com.example.proyecto_gtics.entity.Productos;
import com.example.proyecto_gtics.entity.ProductosSedes;
import com.example.proyecto_gtics.entity.ProductosSedesId;
import com.example.proyecto_gtics.entity.Sedes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductosSedeRepository extends JpaRepository<ProductosSedes,ProductosSedesId> {
    List<ProductosSedes> findBySedes(Sedes sedes);

    List<ProductosSedes> findBySedesAndVisibilidad(Sedes sedes, Integer visibilidad);

    @Query(value = "select productos.idproductos,coalesce(sum(productos_has_sedes.cantidad),0) as cantidadTotal from productos left join productos_has_sedes on productos.idproductos=productos_has_sedes.productos_idproductos group by productos.idproductos;",nativeQuery = true)
    List<CantidadTotalPorProducto> obtenerCantidadTotalPorProducto();

    ProductosSedes findByProductosAndSedes(Productos productos,Sedes sedes);

    /*@Query(value = "SELECT * FROM proyecto_gtics.productos_has_sedes where productos_has_sedes.productos_idproductos=?1 and productos_has_sedes.sedes_idsedes=?2 ;",nativeQuery = true)
    ProductosSedes obtenerProductoSedePorId(Integer idProducto, Integer idSede);*/

    @Query(value = "select * from proyecto_gtics.productos_has_sedes where cantidad<=25 and sedes_idsedes=?1 and visibilidad=1; ",nativeQuery = true)
    List<ProductosSedes> obtenerProductosParaPreorden(Integer idSede);

}
