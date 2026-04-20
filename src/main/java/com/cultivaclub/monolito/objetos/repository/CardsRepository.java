package com.cultivaclub.monolito.objetos.repository;

import com.cultivaclub.monolito.objetos.domain.Cards;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CardsRepository extends JpaRepository<Cards, UUID>, JpaSpecificationExecutor<Cards> {

    @Query("SELECT COUNT(c) FROM Cards c WHERE c.id_usuario = :userId")
    long countByIdUsuario(UUID userId);

    /**
     * Override do {@code findAll(Specification, Pageable)} para forçar
     * o fetch EAGER de {@code tarefas} via EntityGraph — evita
     * LazyInitializationException durante o mapeamento pelo MapStruct.
     */
    @Override
    @EntityGraph(attributePaths = "tarefas")
    Page<Cards> findAll(Specification<Cards> spec, Pageable pageable);

    /**
     * Override do {@code findById} pelo mesmo motivo acima.
     */
    @Override
    @EntityGraph(attributePaths = "tarefas")
    Optional<Cards> findById(UUID id);
}
