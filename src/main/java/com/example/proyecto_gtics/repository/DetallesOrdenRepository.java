package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.DetallesOrden;
import com.example.proyecto_gtics.entity.Ordenes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallesOrdenRepository extends JpaRepository<DetallesOrden,Integer> {

    List<DetallesOrden> findByOrdenes(Ordenes ordenReposicion);

    @Query(value = "SELECT sum(monto_parcial) FROM proyecto_gtics.detalles_orden where ordenes_idordenes = ?1", nativeQuery = true)
    Float calcularMontoTotal(Integer idOrden);

}
