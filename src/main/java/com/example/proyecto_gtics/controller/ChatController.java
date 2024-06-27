package com.example.proyecto_gtics.controller;

import com.example.proyecto_gtics.dto.ChatMessage;
import com.example.proyecto_gtics.entity.Chat;
import com.example.proyecto_gtics.entity.Mensajes;
import com.example.proyecto_gtics.repository.ChatRepository;
import com.example.proyecto_gtics.repository.MensajesRepository;
import com.example.proyecto_gtics.repository.TipoChatRepository;
import com.example.proyecto_gtics.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private MensajesRepository mensajesRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private TipoChatRepository tipoChatRepository;
    @Autowired
    private MessageService messageService;


    //Formatear strings a dates y viceversa
    DateTimeFormatter formatStringToDate = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toFormatter();
    DateTimeFormatter formatDateToSring = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @MessageMapping("/chatFarmacista")
    @SendTo("/topic/messages")
    public Mensajes sendMessage(ChatMessage message) {


        Mensajes savedMessage = messageService.saveMessage(
                message.getChatId(),
                message.getContenido(),
                message.getEstado(),
                message.getSender()
        );

        return savedMessage;
    }


    @GetMapping("/mensajesPrevios")
    public List<Mensajes> getMensajes(@RequestParam(name = "chatId", required = false) Integer chatId) {

        if(chatId == null){
            chatId = 1;
        }

        return messageService.getMessagesByChatId(chatId);
    }






}
