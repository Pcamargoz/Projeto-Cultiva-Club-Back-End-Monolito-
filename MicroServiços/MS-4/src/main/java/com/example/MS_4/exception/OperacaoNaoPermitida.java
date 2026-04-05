package com.example.MS_4.exception;

public class OperacaoNaoPermitida extends RuntimeException {
  public OperacaoNaoPermitida(String message) {
    super(message);
  }
}
