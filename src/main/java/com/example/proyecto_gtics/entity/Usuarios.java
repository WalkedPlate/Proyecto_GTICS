package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuarios" , nullable = false)
    private Integer idUsuarios;

    //Llave foranea
    @Column(name = "estado_idestado",nullable = false)
    private Integer idEstado;

    //Llave foranea
    @Column(name = "sedes_idsedes",nullable = false)
    private Integer idSedes;

    @Column(name = "nombre",nullable = false,length = 45)
    private String nombre;

    @Column(name = "correo",nullable = false,length = 100)
    private String correo;

    @Column(name = "contrasena",nullable = false,length = 64)
    private String contrasena;

    @Column(name = "descripcion",nullable = false,length = 200)
    private String descripcion;

    //Verificar tipo de dato para fotos!
    @Column(name = "foto")
    private String foto;

    @Column(name = "direccion",length = 100)
    private String direccion;

    @Column(name = "distrito_residencia",length = 100)
    private String distritoResidencia;

    @Column(name = "codigo_colegio",length = 100)
    private  String codigoColegio;

    //Llave foranea
    @Column(name = "tipo_usuario_idtipo_usuario")
    private Integer idTipoUsuario;

    @Column(name = "token",length = 200)
    private String token;

    @Column(name = "fecha_registro")
    private String fechaRegistro;

    @Column(name = "seguro",length = 100)
    private String seguro;

    //Llave foranea
    @Column(name = "preferencias_usuario_idpreferencias_usuario")
    private Integer idPreferenciasUsuario;
}
