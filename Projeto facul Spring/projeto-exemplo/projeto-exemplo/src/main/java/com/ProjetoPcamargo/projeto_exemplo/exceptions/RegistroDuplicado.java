package com.ProjetoPcamargo.projeto_exemplo.exceptions;

public class RegistroDuplicado extends RuntimeException{
    public RegistroDuplicado(String message) {
        super(message);
    }
}
