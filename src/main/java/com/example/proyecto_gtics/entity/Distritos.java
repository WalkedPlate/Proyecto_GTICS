package com.example.proyecto_gtics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "distritos")
public class Distritos implements Serializable {
    @Id
    @Column(name = "iddistritos")
    private Integer idDistritos;

    @Column(name = "nombre")
    private String nombre;
}
