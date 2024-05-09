package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
@Entity
@Table(name = "productos")
public class Productos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproductos" , nullable = false)
    private Integer idProductos;

    @ManyToOne
    @JoinColumn(name = "categorias_idcategorias",nullable = false)
    private Categorias categorias;

    @Column(name = "nombre", length = 45,nullable = false)
    private String nombre;

    @Column(name = "codigo",length = 100,nullable = false)
    private String codigo;

    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "descripcion",length = 200,nullable = false)
    private String descripcion;

    @Column(name = "fecha_vencimiento",nullable = false)
    private String fechaVencimiento;

    @Column(name = "precio",nullable = false)
    private float precio;

    @ManyToOne
    @JoinColumn(name = "preferencias_usuario_idpreferencias_usuario")
    private PreferenciasUsuario preferenciasUsuario;

    @Column(name = "estado_producto",nullable = false)
    private String estadoProducto;

    @Column(name = "fotonombre",nullable = true)
    private String fotonombre;

    @Column(name = "fotocontenttype",nullable = true)
    private String fotocontenttype;
}
