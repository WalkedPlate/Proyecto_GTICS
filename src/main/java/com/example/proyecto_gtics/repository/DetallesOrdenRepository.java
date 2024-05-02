package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.DetallesOrden;
import com.example.proyecto_gtics.entity.Ordenes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallesOrdenRepository extends JpaRepository<DetallesOrden,Integer> {

    List<DetallesOrden> findByOrdenes(Ordenes ordenReposicion);
}
