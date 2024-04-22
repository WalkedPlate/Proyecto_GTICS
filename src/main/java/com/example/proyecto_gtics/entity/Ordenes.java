package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ordenes")
public class Ordenes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idordenes" , nullable = false)
    private Integer idordenes;

    //Revisar si se debe poner alguna anotacion para las llaves foraneas
    @Column(name = "usuarios_id_usuario",nullable = false)
    private Integer idUsuarios;

    @Column(name = "estado_orden_idestado_orden",nullable = false)
    private Integer idEstadoOrden;

    @Column(name = "tipo_orden_idtipo_orden",nullable = false)
    private Integer idTipoOrden;

    @Column(name = "codigo",nullable = false,length = 150)
    private String codigo;

    @Column(name = "direccion",nullable = false,length = 45)
    private String direccion;

    @Column(name = "monto",nullable = false)
    private float monto;

    //Verificar el tipo de dato de este atributo!!
    @Column(name = "recurrente",nullable = false)
    private boolean recurrente;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "tipo_cobro_idtipo_cobro",nullable = false)
    private Integer idTipoCobro;

    @Column(name = "fecha_registro",nullable = false)
    private String fechaRegistro;

    @Column(name = "foto_receta",nullable = false)
    private String fotoReceta;

    @Column(name = "iddocto",nullable = false)
    private Integer idDoctor;


}
