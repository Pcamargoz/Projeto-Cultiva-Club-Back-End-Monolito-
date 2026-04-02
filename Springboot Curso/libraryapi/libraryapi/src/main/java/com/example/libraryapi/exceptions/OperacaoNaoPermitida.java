package com.example.libraryapi.exceptions;

public class OperacaoNaoPermitida extends RuntimeException{
    public OperacaoNaoPermitida(String message){
        super(message);
    }
}
