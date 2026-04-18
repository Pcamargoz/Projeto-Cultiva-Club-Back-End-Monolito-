package com.cultivaclub.monolito.usuarios.web.dto;

import com.cultivaclub.monolito.usuarios.domain.ROLES;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResultadoPesquisaDeUsuarioDTO(
        UUID id,
        String login,
        String email,
        String nome,
        LocalDateTime dataCadastro,
        ROLES roles
) {}
