package com.cultivaclub.monolito.objetos.web.dto;

import com.cultivaclub.monolito.objetos.domain.TIPO_ALIMENTOS;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CardRespostaDTO(
        UUID id,
        String titulo,
        TIPO_ALIMENTOS alimento,
        UUID idUsuario,
        String anotacoes,
        List<TarefaRespostaDTO> tarefas,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}
