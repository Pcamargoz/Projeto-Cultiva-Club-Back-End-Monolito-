package com.cultivaclub.monolito.objetos.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TarefaDTO(
        @NotBlank @Size(max = 150) String titulo,
        @Size(max = 500) String descricao,
        LocalDateTime dataLimite
) {}
