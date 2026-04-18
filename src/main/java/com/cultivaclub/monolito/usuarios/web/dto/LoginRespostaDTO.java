package com.cultivaclub.monolito.usuarios.web.dto;

import com.cultivaclub.monolito.usuarios.domain.ROLES;

import java.util.UUID;

public record LoginRespostaDTO(
        UUID id,
        String login,
        String email,
        String nome,
        ROLES role,
        String token
) {}
