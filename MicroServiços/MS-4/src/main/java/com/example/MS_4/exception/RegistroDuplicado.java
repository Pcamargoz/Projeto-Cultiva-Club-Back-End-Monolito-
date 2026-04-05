package com.example.MS_4.exception;

public class RegistroDuplicado extends RuntimeException {
  public RegistroDuplicado(String message) {
    super(message);
  }
}
