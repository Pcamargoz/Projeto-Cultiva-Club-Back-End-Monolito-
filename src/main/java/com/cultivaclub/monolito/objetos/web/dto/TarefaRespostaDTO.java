package com.cultivaclub.monolito.objetos.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TarefaRespostaDTO(
        UUID id,
        String titulo,
        String descricao,
        Boolean concluida,
        LocalDateTime dataLimite,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {}
