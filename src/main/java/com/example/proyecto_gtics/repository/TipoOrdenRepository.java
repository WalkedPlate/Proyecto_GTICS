package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.entity.TipoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoOrdenRepository extends JpaRepository<TipoOrden,Integer> {

}
