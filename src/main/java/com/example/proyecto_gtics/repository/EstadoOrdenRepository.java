package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoOrdenRepository extends JpaRepository<EstadoOrden,Integer> {

    EstadoOrden findByIdEstadoOrden(int idEstadoOrden);
}
