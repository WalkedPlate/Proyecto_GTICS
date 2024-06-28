package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "ordenes")
public class Ordenes implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idordenes" , nullable = false)
    private Integer idordenes;

    @ManyToOne
    @JoinColumn(name = "usuarios_id_usuario",nullable = false)
    private Usuarios usuarios;

    @ManyToOne
    @JoinColumn(name = "estado_orden_idestado_orden")
    private EstadoOrden estadoOrden;

    @ManyToOne
    @JoinColumn(name = "tipo_orden_idtipo_orden",nullable = false)
    private TipoOrden tipoOrden;

    @Column(name = "codigo",nullable = false,length = 150)
    private String codigo;

    @Column(name = "direccion",length = 45)
    private String direccion;

    @Column(name = "monto")
    private Float monto;

    @Column(name = "recurrente")
    private Boolean recurrente;

    @Column(name = "telefono")
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "tipo_cobro_idtipo_cobro",nullable = false)
    private TipoCobro tipoCobro;

    @Column(name = "fecha_registro",nullable = false)
    private String fechaRegistro;

    @Column(name = "foto_receta")
    private byte[] fotoReceta;

    @OneToOne
    @JoinColumn(name = "iddoctor")
    private Usuarios doctor;

    @Column(name = "fecha_entrega")
    private String fechaEntrega;

    @ManyToOne
    @JoinColumn(name = "sedes_idsedes")
    private Sedes sedes;

    @Column(name = "fotonombre",nullable = true)
    private String fotonombre;

    @Column(name = "fotocontenttype",nullable = true)
    private String fotocontenttype;

}
