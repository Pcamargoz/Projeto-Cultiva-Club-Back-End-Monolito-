package com.example.libraryapi.controller.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

// por ser record podemos fazer esse erros reposta em outras classes
public record ErroResposta(int status, String mensage, List<ErroCampo> erros){

    // criando respostas para cada erro diferente
    public static ErroResposta respostaPadrao(String mensagem){
        return new ErroResposta(HttpStatus.BAD_REQUEST.value(), mensagem, List.of());
    }


    public static ErroResposta conflito(String mensagem){
        // preenchemos o metodo com a mensagem que quisermos
        // geralmente a mensagem do e.getmessagem
        return new ErroResposta(HttpStatus.CONFLICT.value(), mensagem, List.of());
    }


}
