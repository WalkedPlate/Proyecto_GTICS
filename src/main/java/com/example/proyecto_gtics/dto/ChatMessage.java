package com.example.proyecto_gtics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    private Integer chatId;
    private String contenido;
    private String estado;
    private Integer sender;


}
