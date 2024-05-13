package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

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
    @NotBlank
    @Size(max= 44, message = "El nombre no puede ser muy largo")
    private String nombre;



    @Column(name = "correo",nullable = false,length = 100, unique = true)
    @NotBlank
    @Size(max= 99, message = "El correo no puede ser muy largo")
    private String correo;


    @Column(name = "contrasena",nullable = false,length = 64)
    //@NotBlank
    @Size(max= 63, message = "La contraseña no valida")
    private String contrasena;


    @Column(name = "descripcion",nullable = true,length = 200)
    @Size(max= 199, message = "Descripcion muy alrga")
    private String descripcion;

    //Verificar tipo de dato para fotos!
    @Column(name = "foto")
    //@Size(max= 200, message = "Descripcion muy larga")
    private byte[] foto;




    @Column(name = "direccion",length = 100)
    @Size(max= 99, message = "direccion muy larga")
    private String direccion;


    @Column(name = "distrito_residencia",length = 100)
    @Size(max= 99, message = "distrito muy largo")
    private String distritoResidencia;

    @Column(name = "codigo_colegio",length = 100)
    //@Pattern(regexp = "\\d{6}", message = "El código del colegio debe ser un número entero de 6 digitos")
    private  String codigoColegio;

    @ManyToOne
    @JoinColumn(name = "tipo_usuario_idtipo_usuario",nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(name = "token",length = 200)
    @Size(max = 199)
    private String token;

    @Column(name = "fecha_registro")
    private Date fechaRegistro; //Cambio de String -> Date / Si causa algun problema regresarlo a String!!

    @Column(name = "seguro",length = 100)
    @Size(max = 99)
    private String seguro;

    @Column(name = "dni")
    @Digits(integer = 10, fraction = 0)
    @Positive
    @Range(min = 10000000, max = 99999999, message = "El DNI no es valido")
    private Integer dni;

    //@OneToOne
    //@JoinColumn(name = "preferencias_usuario_idpreferencias_usuario",nullable = false)
    @Column(name = "preferencias_usuario_idpreferencias_usuario")
    private Integer preferenciasUsuario;

    @Column(name = "dias_ban")
    private Integer diasBan;

    @Column(name = "fecha_ban")
    private Date fechaBan;

    @Column(name = "fotonombre",nullable = true)
    private String fotonombre;

    @Column(name = "fotocontenttype",nullable = true)
    private String fotocontenttype;

}
