package com.example.libraryapi.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public interface GeneriController {

    // gerando um metodo generico para traçar a rota da requisição rest web
    default URI gerarHeaderLocation(UUID id){
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

    }



}
