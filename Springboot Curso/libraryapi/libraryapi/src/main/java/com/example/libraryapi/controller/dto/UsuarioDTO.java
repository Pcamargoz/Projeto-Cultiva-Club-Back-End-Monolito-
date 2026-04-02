package com.example.libraryapi.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UsuarioDTO(

        @NotBlank(message = "Login obrigatório")
        String login,

        @NotBlank(message = "Senha obrigatória")
        String senha,

        @NotBlank(message = "Email obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotEmpty(message = "Informe pelo menos uma role")
        List<String> roles

) {}