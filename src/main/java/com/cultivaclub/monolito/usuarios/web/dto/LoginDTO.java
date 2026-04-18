package com.cultivaclub.monolito.usuarios.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "Login obrigatório")
        String login,

        @NotBlank(message = "Senha obrigatória")
        String senha
) {}
