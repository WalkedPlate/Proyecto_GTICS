package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.dto.CantidadTotalPorProducto;
import com.example.proyecto_gtics.entity.Productos;
import com.example.proyecto_gtics.entity.ProductosSedes;
import com.example.proyecto_gtics.entity.ProductosSedesId;
import com.example.proyecto_gtics.entity.Sedes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductosSedeRepository extends JpaRepository<ProductosSedes,ProductosSedesId> {
    List<ProductosSedes> findBySedes(Sedes sedes);

    @Query(value = "select productos.idproductos,coalesce(sum(productos_has_sedes.cantidad),0) as cantidadTotal from productos left join productos_has_sedes on productos.idproductos=productos_has_sedes.productos_idproductos group by productos.idproductos;",nativeQuery = true)
    List<CantidadTotalPorProducto> obtenerCantidadTotalPorProducto();

    ProductosSedes findByProductosAndSedes(Productos productos,Sedes sedes);
}
