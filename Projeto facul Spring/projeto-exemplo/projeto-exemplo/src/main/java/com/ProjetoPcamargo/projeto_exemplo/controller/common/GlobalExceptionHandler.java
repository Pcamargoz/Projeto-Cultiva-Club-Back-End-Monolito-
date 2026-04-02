package com.ProjetoPcamargo.projeto_exemplo.controller.common;

import com.ProjetoPcamargo.projeto_exemplo.controller.DTO.ErroCampo;
import com.ProjetoPcamargo.projeto_exemplo.controller.DTO.ErroRespostas;
import com.ProjetoPcamargo.projeto_exemplo.exceptions.OperacaoNaoPermitida;
import com.ProjetoPcamargo.projeto_exemplo.exceptions.RecursoNaoEcontrado;
import com.ProjetoPcamargo.projeto_exemplo.exceptions.RegistroDuplicado;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

// pega erros rest e retorna respostas rest
// so de fazer isso ele ja trabalha por si só
@RestControllerAdvice
public class GlobalExceptionHandler {
    // aqui vai estar todos os erros que o spring vai captar em todo a api


    @ExceptionHandler(MethodArgumentNotValidException.class)

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroRespostas handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        List<FieldError> fieldErrors = e.getFieldErrors();

        List<ErroCampo> listaErros = fieldErrors.stream().map(fe -> new ErroCampo(fe.getField(),fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErroRespostas(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", listaErros);
    }
    @ExceptionHandler(RegistroDuplicado.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroRespostas handleRegistroDuplicado(RegistroDuplicado e){
        return ErroRespostas.conflito(e.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitida.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroRespostas handleOperacaoNaoPermitida(OperacaoNaoPermitida e){
        return ErroRespostas.respostaPadrao(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroRespostas hadleErrosNaoTratados(RuntimeException e){
        return new ErroRespostas(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro inesperad ! Favor entrar em contato com o suporte ! ",List.of());
    }

    @ExceptionHandler(RecursoNaoEcontrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroRespostas handleErro404(RecursoNaoEcontrado e){
        return new ErroRespostas(HttpStatus.NOT_FOUND.value(),
                " Recurso Não Econtrado ",List.of());
    }
}
