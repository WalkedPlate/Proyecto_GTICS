package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.dto.*;
import com.example.proyecto_gtics.entity.Categorias;
import com.example.proyecto_gtics.entity.Productos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value="select productos.idproductos,productos.nombre, sum(productos_has_sedes.cantidad) as cantidadTotal from productos inner join productos_has_sedes on productos.idproductos = productos_has_sedes.productos_idproductos where productos_has_sedes.sedes_idsedes=?1 and productos_has_sedes.visibilidad=1 group by productos.idproductos , productos.nombre having cantidadTotal < 25;",nativeQuery = true)
    List<CantProductoMenosPorSede> obtenerProductosPocoInventariado(Integer idSede);

    @Query(value="SELECT p.nombre, p.descripcion, p.idproductos, p.foto, c.nombre AS nombrecategoria, c.idcategorias, p.precio, COUNT(do.productos_idproductos) AS cantidad_ordenes \n" +
            "FROM detalles_orden do \n" +
            "JOIN productos p ON do.productos_idproductos = p.idproductos \n" +
            "JOIN categorias c ON p.categorias_idcategorias = c.idcategorias \n" +
            "JOIN preferencias_usuario pu ON p.preferencias_usuario_idpreferencias_usuario = pu.idpreferencias_usuario \n" +
            "WHERE p.estado_producto = 'activo' \n" +
            "AND pu.descripcion = 'Bueno'  \n" +
            "GROUP BY p.idproductos \n" +
            "ORDER BY cantidad_ordenes DESC \n" +
            "LIMIT 5;",nativeQuery = true)
    List<ProductosTendencia> obtenerProductosMasComprados();

    @Query(value="SELECT p.idproductos,p.nombre,p.precio,p.foto,pu.descripcion AS preferencia_usuario,pu.idpreferencias_usuario,c.nombre AS categoria, c.idcategorias FROM productos p JOIN preferencias_usuario pu ON p.preferencias_usuario_idpreferencias_usuario = pu.idpreferencias_usuario JOIN categorias c ON p.categorias_idcategorias = c.idcategorias WHERE pu.descripcion = 'Bueno' AND p.estado_producto='Activo' LIMIT 5;",nativeQuery = true)
    List<ProductosMejorValorados> obtenerProductosMejorValorados();

    @Query(value="SELECT p.idproductos,p.nombre,p.precio,p.foto,pu.descripcion AS preferencia_usuario,pu.idpreferencias_usuario,c.nombre AS categoria, c.idcategorias FROM productos p JOIN preferencias_usuario pu ON p.preferencias_usuario_idpreferencias_usuario = pu.idpreferencias_usuario JOIN categorias c ON p.categorias_idcategorias = c.idcategorias WHERE pu.descripcion = 'Bueno' AND p.estado_producto='Activo' LIMIT 4;",nativeQuery = true)
    List<ProductosMejorValorados> obtenerProductosMejorValorados2();

    @Query(value="SELECT p.idproductos,p.nombre,p.precio,p.foto, c.nombre AS categoria, c.idcategorias FROM productos p JOIN categorias c ON p.categorias_idcategorias = c.idcategorias WHERE p.estado_producto='Activo' ORDER BY p.idproductos DESC LIMIT 5;",nativeQuery = true)
    List<ProductosAnadidosRecientemente> obtenerProductosRecientes();

    @Query(value = "SELECT p.nombre, p.descripcion, p.idproductos, p.precio, p.foto, c.nombre AS categoria, c.idcategorias, COUNT(do.iddetalles_orden) AS total_ordenes " +
            "FROM productos p " +
            "JOIN categorias c ON p.categorias_idcategorias = c.idcategorias " +
            "JOIN detalles_orden do ON p.idproductos = do.productos_idproductos " +
            "JOIN preferencias_usuario pu ON p.preferencias_usuario_idpreferencias_usuario = pu.idpreferencias_usuario " +
            "WHERE pu.descripcion = 'Bueno' AND p.estado_producto='Activo'" +
            "GROUP BY p.idproductos, p.nombre " +
            "ORDER BY total_ordenes DESC, MAX(do.iddetalles_orden) DESC " +
            "LIMIT 1", nativeQuery = true)
    ProductoMejorVendido obtenerProductoMejorVendido();

    Productos findByIdProductos(Integer idProducto);

    @Query(value = "SELECT p.* FROM productos p JOIN categorias c ON p.categorias_idcategorias = c.idcategorias WHERE p.nombre LIKE '%:nombre%' OR c.nombre LIKE '%:nombre%' LIMIT 0, 1000;", nativeQuery = true)
    List<Productos> searchByKeyword(@Param("nombre") String nombre);

    Page<Productos> findByCategorias(Categorias categorias, Pageable pageable);

    @Query(value = "SELECT p.* FROM productos p " +
            "JOIN categorias c ON p.categorias_idcategorias = c.idcategorias " +
            "JOIN (SELECT idproducto, AVG(preferencia) AS promedio_preferencia " +
            "FROM preferencias_usuario " +
            "GROUP BY idproducto) pu ON p.idProductos = pu.idproducto " +
            "WHERE p.categorias_idcategorias = :idCategoria " +
            "ORDER BY pu.promedio_preferencia DESC, RAND() " +
            "LIMIT 4", nativeQuery = true)
    List<Productos> findTop4ProductosByCategoria2(@Param("idCategoria") int idCategoria);

    @Query("SELECT p FROM Productos p WHERE p.categorias.idCategorias = :idCategorias ORDER BY FUNCTION('RAND')")
    List<Productos> findTop4ProductosByCategoria(@Param("idCategorias") int idCategorias, Pageable pageable);


}