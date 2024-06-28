package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.Chat;
import com.example.proyecto_gtics.entity.Ordenes;
import com.example.proyecto_gtics.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    List<Chat> findByUsuario1(Usuarios usuarios);
    List<Chat> findByUsuario2(Usuarios usuarios);

    Optional<Chat> findByUsuario1AndAndUsuario2(Usuarios usuario1, Usuarios usuario2);

    Chat findFirstByOrderByIdChatDesc(); // recuperar la última entrada de la tabla

    Optional<Chat> findFirstByUsuario1OrderByIdChatDesc(Usuarios usuarios); // Busca último chat del farmacista
    Optional<Chat> findFirstByUsuario2OrderByIdChatDesc(Usuarios usuarios); // Busca último chat del paciente

}
