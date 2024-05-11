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

    @Query(value = "select sum(cantidad) as cantidadTotal, productos_idproductos as idProductoDTO from proyecto_gtics.productos_has_sedes group by productos_idproductos;",nativeQuery = true)
    List<CantidadTotalPorProducto> obtenerCantidadTotalPorProducto();

    ProductosSedes findByProductosAndSedes(Productos productos,Sedes sedes);
}
