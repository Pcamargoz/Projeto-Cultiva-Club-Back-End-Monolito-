package com.ProjetoPcamargo.projeto_exemplo.exceptions;

public class OperacaoNaoPermitida extends RuntimeException{
    public OperacaoNaoPermitida(String message){
        super(message);
    }
}
