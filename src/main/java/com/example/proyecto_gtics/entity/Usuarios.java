package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario" , nullable = false)
    private Integer idUsuario;

    @ManyToOne
    @JoinColumn(name = "estado_idestado",nullable = false)
    private EstadoUsuario estadoUsuario;

    @ManyToOne
    @JoinColumn(name = "sedes_idsedes")
    private Sedes sedes;

    @Column(name = "nombre",nullable = false,length = 45)
    private String nombre;

    @Column(name = "correo",nullable = false,length = 100)
    private String correo;

    @Column(name = "contrasena",nullable = false,length = 64)
    private String contrasena;

    @Column(name = "descripcion",nullable = true,length = 200)
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

    @ManyToOne
    @JoinColumn(name = "tipo_usuario_idtipo_usuario",nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(name = "token",length = 200)
    private String token;

    @Column(name = "fecha_registro")
    private Date fechaRegistro; //Cambio de String -> Date / Si causa algun problema regresarlo a String!!

    @Column(name = "seguro",length = 100)
    private String seguro;

    @Column(name = "dni")
    private String dni;

    //@OneToOne
    //@JoinColumn(name = "preferencias_usuario_idpreferencias_usuario",nullable = false)
    @Column(name = "preferencias_usuario_idpreferencias_usuario")
    private Integer preferenciasUsuario;

}
