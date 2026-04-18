package com.cultivaclub.monolito.common.web;

import com.cultivaclub.monolito.common.exception.OperacaoNaoPermitida;
import com.cultivaclub.monolito.common.exception.RecursoNaoEncontrado;
import com.cultivaclub.monolito.common.exception.RegistroDuplicado;
import com.cultivaclub.monolito.common.web.dto.ErroCampoDTO;
import com.cultivaclub.monolito.common.web.dto.ErroRespostaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistroDuplicado.class)
    public ResponseEntity<ErroRespostaDTO> handleRegistroDuplicado(RegistroDuplicado ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErroRespostaDTO.conflito(ex.getMessage()));
    }

    @ExceptionHandler(RecursoNaoEncontrado.class)
    public ResponseEntity<ErroRespostaDTO> handleRecursoNaoEncontrado(RecursoNaoEncontrado ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroRespostaDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage(), List.of()));
    }

    @ExceptionHandler(OperacaoNaoPermitida.class)
    public ResponseEntity<ErroRespostaDTO> handleOperacaoNaoPermitida(OperacaoNaoPermitida ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErroRespostaDTO(HttpStatus.FORBIDDEN.value(), ex.getMessage(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDTO> handleValidation(MethodArgumentNotValidException ex) {
        List<ErroCampoDTO> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErroCampoDTO(fe.getDefaultMessage(), fe.getField()))
                .toList();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroRespostaDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", erros));
    }
}
