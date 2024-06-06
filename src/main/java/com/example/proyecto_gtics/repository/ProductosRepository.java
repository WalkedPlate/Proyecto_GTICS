package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.dto.CantProductoMenosPorSede;
import com.example.proyecto_gtics.entity.Categorias;
import com.example.proyecto_gtics.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductosRepository extends JpaRepository<Productos,Integer> {
    //List<Productos> findByNombre(String nombre);
    List<Productos> findByCategorias(Categorias categorias);

    List<Productos> findByEstadoProducto(String estadoProducto);
    long countByCategorias(Categorias categorias);

    //QueryNativo
    @Query(value = "SELECT p from productos p where upper(p.nombre) like upper(concat('%',?1,'%'))",nativeQuery = true)
    List<Productos> searchByFullNameQueryNative(String nombre);

    //SpringData JPA
    List<Productos> findByNombreContainingIgnoreCase(String nombre);

    Productos findByCodigo(String codigo);

    Productos findByNombre(String nombre);

    @Query(value="select productos.idproductos,productos.nombre, sum(productos_has_sedes.cantidad) as cantidadTotal from productos inner join productos_has_sedes on productos.idproductos = productos_has_sedes.productos_idproductos group by productos.idproductos , productos.nombre having cantidadTotal < 200;",nativeQuery = true)
    List<CantProductoMenosPorSede> obtenerProductosPocoInventariado();

}