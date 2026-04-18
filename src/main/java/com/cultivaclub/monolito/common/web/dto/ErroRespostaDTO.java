package com.cultivaclub.monolito.common.web.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErroRespostaDTO(int status, String message, List<ErroCampoDTO> erros) {

    public static ErroRespostaDTO respostaPadrao(String message) {
        return new ErroRespostaDTO(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    public static ErroRespostaDTO conflito(String message) {
        return new ErroRespostaDTO(HttpStatus.CONFLICT.value(), message, List.of());
    }
}
