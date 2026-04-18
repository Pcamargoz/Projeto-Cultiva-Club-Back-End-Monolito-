package com.cultivaclub.monolito.usuarios.web.dto;

import com.cultivaclub.monolito.usuarios.domain.ROLES;
import jakarta.validation.constraints.NotNull;

public record AtualizarRoleDTO(
        @NotNull(message = "Role é obrigatória") ROLES role
) {}
