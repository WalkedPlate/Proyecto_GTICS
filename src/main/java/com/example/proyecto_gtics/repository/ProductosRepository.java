package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.Categorias;
import com.example.proyecto_gtics.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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



}
