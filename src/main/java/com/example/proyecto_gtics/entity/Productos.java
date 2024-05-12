package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jdk.jfr.ContentType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
    @NotBlank
    @Size(max= 45, message = "Nombre no valido")
    private String nombre;

    @Column(name = "codigo",length = 100,nullable = false)
    @NotBlank
    @Size(max= 100, message = "Codigo no valido")
    private String codigo;

    @Column(name = "foto")
    @Size(max = 134217728, message = "La foto no puede exceder 128 MB")
    //@ContentType(allowed = {"image/jpeg", "image/png", "image/gif"}, message = "El archivo adjunto debe ser una imagen (JPEG, PNG o GIF)")
    private byte[] foto;

    @Column(name = "descripcion",length = 200,nullable = false)
    @NotBlank
    @Size(max= 200, message = "Codigo no valido")
    private String descripcion;

    @Column(name = "fecha_vencimiento",nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Ajusta el patrón según el formato esperado de la fecha
    @NotBlank
    private String fechaVencimiento;

    @Column(name = "precio",nullable = false)
    @NotNull
    //@Digits(integer = 10,fraction = 2,message = "Precio No valido")
    @Positive
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
