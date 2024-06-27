package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mensajes")
public class Mensajes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmensajes" , nullable = false)
    private Integer idMensajes;

    @Column(name = "contenido" , nullable = false)
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "chat_idchat", nullable = false)
    private Chat chat;

    @Column(name = "fecha" , nullable = true)
    private String fecha;

    @Column(name = "estado" , nullable = true)
    private String estado;

    @Column(name = "sender" , nullable = true)
    private Integer sender;
}
