package com.example.proyecto_gtics.repository;

import com.example.proyecto_gtics.entity.TipoChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoChatRepository extends JpaRepository<TipoChat,Integer> {
}
