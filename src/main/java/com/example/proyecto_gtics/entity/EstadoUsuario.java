package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "estado_usuario")
public class EstadoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idestado_usuario" , nullable = false)
    private Integer idEstadoUsuario;

    @Column(name = "nombre",nullable = false,length = 80)
    private String nombre;
}
