package com.ProjetoPcamargo.projeto_exemplo.controller.DTO;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErroRespostas(int status, String message, List<ErroCampo> erros) {

    public static ErroRespostas respostaPadrao(String message){

        return new ErroRespostas(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    public static ErroRespostas conflito(String message){
        // o que vai ser retornado no body
        return new ErroRespostas(HttpStatus.CONFLICT.value(), message, List.of());
    }
}
