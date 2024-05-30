package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "estado_usuario")
public class EstadoUsuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idestado_usuario" , nullable = false)
    private String idEstadoUsuario;

    /*@Column(name = "dias_ban")
    private Integer diasBan;

    @Column(name = "fecha_ban")
    private Date fechaBan;*/

}
