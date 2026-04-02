package com.example.libraryapi.controller.dto;

import com.example.libraryapi.model.GeneroLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO(
        // VALIDADOR DE ISBN DO PROPRIO SPRING
        @ISBN
        @NotBlank(message = "Campo Obrigatorio")
        String isbn,
        // not blank dispersa (não aceita) tanto campos vazios e nulos
        @NotBlank(message = "Campo Obrigatorio")
        String titulo,
        // dispersa campos nulos apenas , ou seja não aceita nada nulo
        @NotNull(message = "Campo Obrigatorio")
        @Past(message = "Não pode ser data futura ")
        LocalDate dataPublicacao,
        GeneroLivro genero,
        BigDecimal preco,
        @NotNull(message = "Campo Obrigatorio")
        UUID idAutor) {
}
