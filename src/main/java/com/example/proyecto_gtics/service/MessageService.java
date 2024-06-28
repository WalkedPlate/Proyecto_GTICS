package com.example.proyecto_gtics.service;

import com.example.proyecto_gtics.entity.Chat;
import com.example.proyecto_gtics.entity.Mensajes;
import com.example.proyecto_gtics.entity.Usuarios;
import com.example.proyecto_gtics.repository.ChatRepository;
import com.example.proyecto_gtics.repository.MensajesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Objects;

@Service
public class MessageService {

    //Formatear strings a dates
    DateTimeFormatter formatStringToDate = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toFormatter();
    DateTimeFormatter formatDateToSring = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MensajesRepository mensajesRepository;

    @Autowired
    private ChatRepository chatRepository;

    public Mensajes saveMessage(Integer chatId, String content, String state, Integer sender) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));

        Mensajes mensaje = new Mensajes();
        mensaje.setContenido(content);
        LocalDateTime fechaActual = LocalDateTime.now(ZoneId.of("America/Lima")); //sacamos la fecha actual
        mensaje.setFecha(fechaActual.format(formatStringToDate));
        mensaje.setEstado(state);
        mensaje.setChat(chat);
        mensaje.setSender(sender);

        return mensajesRepository.save(mensaje);
    }

    public List<Mensajes> getMessagesByChatId(Integer chatId) {
        Chat chat = chatRepository.findById(chatId).get();
        return mensajesRepository.findByChat(chat);
    }


    public boolean verificarAccesoChat(Integer chatId, Usuarios usuario) {

        Chat chat = chatRepository.findById(chatId).get();
        boolean autorizado = false;

        for(Chat i : chatRepository.findByUsuario1OrderByIdChatDesc(usuario)){
            if(Objects.equals(i.getIdChat(), chatId)){
                autorizado = true;
            }
        }
        for(Chat i : chatRepository.findByUsuario2OrderByIdChatDesc(usuario)){
            if(Objects.equals(i.getIdChat(), chatId)){
                autorizado = true;
            }
        }
        return autorizado;
    }
}
