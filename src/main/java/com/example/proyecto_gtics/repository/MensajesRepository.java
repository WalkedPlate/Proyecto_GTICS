package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.Chat;
import com.example.proyecto_gtics.entity.Mensajes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajesRepository extends JpaRepository<Mensajes, Integer> {

    List<Mensajes> findByChat(Chat chat);
}
