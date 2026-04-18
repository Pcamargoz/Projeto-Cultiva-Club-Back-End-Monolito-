package com.cultivaclub.monolito.objetos.repository;

import com.cultivaclub.monolito.objetos.domain.Tarefas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TarefasRepository extends JpaRepository<Tarefas, UUID> {

    @Query("SELECT t FROM Tarefas t WHERE t.card.id_usuario = :userId AND t.dataLimite >= :inicio AND t.dataLimite < :fim")
    List<Tarefas> findByUsuarioAndDataLimiteBetween(UUID userId, LocalDateTime inicio, LocalDateTime fim);
}
