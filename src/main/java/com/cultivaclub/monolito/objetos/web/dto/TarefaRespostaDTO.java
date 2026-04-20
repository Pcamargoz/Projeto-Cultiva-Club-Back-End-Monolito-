package com.cultivaclub.monolito.objetos.web.dto;

import com.cultivaclub.monolito.objetos.domain.STATUS_TAREFAS;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de resposta de tarefa.
 *
 * Campos:
 *   - status   : novo — reflete o enum STATUS_TAREFAS armazenado no banco.
 *   - concluida: derivado do status, mantido por compatibilidade com o
 *                frontend atual. Equivale a {@code status == CONCLUIDO}.
 */
public record TarefaRespostaDTO(
        UUID id,
        String titulo,
        String descricao,
        STATUS_TAREFAS status,
        Boolean concluida,
        LocalDateTime dataLimite,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}
