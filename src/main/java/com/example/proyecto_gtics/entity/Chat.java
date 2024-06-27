package com.example.proyecto_gtics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idchat" , nullable = false)
    private Integer idChat;

    @ManyToOne
    @JoinColumn(name = "usuarios_id_usuario", nullable = false)
    private Usuarios usuario1;

    @ManyToOne
    @JoinColumn(name = "idusuario_2")
    private Usuarios usuario2;

    @ManyToOne
    @JoinColumn(name = "tipo_chat_idtipo_chat" , nullable = false)
    private TipoChat tipoChat;

}
