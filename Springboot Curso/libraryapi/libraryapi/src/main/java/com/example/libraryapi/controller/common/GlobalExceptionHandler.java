package com.example.libraryapi.controller.common;

import com.example.libraryapi.controller.dto.ErroCampo;
import com.example.libraryapi.controller.dto.ErroResposta;
import com.example.libraryapi.exceptions.CampoInvalidoException;
import com.example.libraryapi.exceptions.OperacaoNaoPermitida;
import com.example.libraryapi.exceptions.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

// vai capturar exceptions rest e vai retornar resposta rest
@RestControllerAdvice
public class GlobalExceptionHandler {

    // faz ele capturar o erro e jogar na variavel o erro
    // tambem precisando especificar o erro que vamos capturar
    @ExceptionHandler(MethodArgumentNotValidException.class)
    // como sempre vo pegar o mesmo codigo com isso aqui posso ja falar diretamente
    // o codigo do erro para ja mapearmos
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    // campos que nao foram preenchidos
    public ErroResposta handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        // aqui ele esta pegando o campo que falta ser preenchido
        List<FieldError> fieldErrors = e.getFieldErrors();

        // estamos colocando ele em um erro de campo aonde ele vai falar por mesagem o cammpo que falta
        // por conta que no construtor colocamos string e depois vamos mandar uma mensagem
        // aonde pegamos tambem do metodo com default get message
        // para cada campo vazio ele pegar o campo e a mensagem do campo fazio e adicionar na lista
        List<ErroCampo> listaErros = fieldErrors.stream().map(fe -> new ErroCampo(fe.getField(),fe.getDefaultMessage()))
                .collect(Collectors.toList());

        // e retornando o erro resposta caso de um erro de validação no metodo
        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de Validação",
                listaErros);

    }
    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleCampoInvalidException(CampoInvalidoException e){
        // retornando o erro de resposta rest , a mensagem a e lista do campo
        return new ErroResposta(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação. ",
                List.of(new ErroCampo(e.getCampo(),e.getMessage())));
    }

    // objeto que vamos receber como parametro
    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta handleRegistroDuplicadoException(RegistroDuplicadoException e){
        return ErroResposta.conflito(e.getMessage());

    }

    @ExceptionHandler(OperacaoNaoPermitida.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta handleOperacaoNaoPermitida(OperacaoNaoPermitida e){
        return ErroResposta.respostaPadrao(e.getMessage());
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErroResposta handleAccesDeniedException(AccessDeniedException e){
        return new ErroResposta(HttpStatus.FORBIDDEN.value(), "Acesso Negado", List.of());
    }
    // erros que nao foram tratados ele cai aqui
    /*@ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleErrosNaoTratados(RuntimeException e){
        return new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                " Ocorreu um erro inesperado , entre em contato com a administração do sistema ! ", List.of());

    }*/
    // usando esse para que a gente possa ver o stacktrace
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta handleErrosNaoTratados(RuntimeException e){

        e.printStackTrace(); // MOSTRA O ERRO REAL NO CONSOLE

        return new ErroResposta(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                List.of()
        );
    }
}
