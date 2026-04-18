package com.cultivaclub.monolito.objetos.web.dto;

import com.cultivaclub.monolito.objetos.domain.TIPO_ALIMENTOS;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CardDTO(
        @NotBlank @Size(max = 100) String titulo,
        @NotNull TIPO_ALIMENTOS alimento,
        @NotNull UUID idUsuario,
        @NotBlank @Size(max = 1000) String anotacoes
) {}
