package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "preferencias_usuario")
public class PreferenciasUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpreferencias_usuario" , nullable = false)
    private Integer idPreferenciasUsuario;

    @Column(name = "descripcion",length = 100)
    private String descripcion;
}
