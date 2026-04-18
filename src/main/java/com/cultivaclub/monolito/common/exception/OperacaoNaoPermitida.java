package com.cultivaclub.monolito.common.exception;

public class OperacaoNaoPermitida extends RuntimeException {
    public OperacaoNaoPermitida(String message) {
        super(message);
    }
}
