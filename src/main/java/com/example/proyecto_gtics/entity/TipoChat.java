package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tipo_chat")
public class TipoChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_chat" , nullable = false)
    private Integer id;

    @Column(name = "nombre" , nullable = false)
    private String nombre;
}
