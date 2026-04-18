package com.cultivaclub.monolito.objetos.repository;

import com.cultivaclub.monolito.objetos.domain.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CardsRepository extends JpaRepository<Cards, UUID>, JpaSpecificationExecutor<Cards> {

    @Query("SELECT COUNT(c) FROM Cards c WHERE c.id_usuario = :userId")
    long countByIdUsuario(UUID userId);
}
