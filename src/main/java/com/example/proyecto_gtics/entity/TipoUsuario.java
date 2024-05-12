package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tipo_usuario")
public class TipoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipo_usuario" , nullable = false)
    private String idTipoUsuario;

}
