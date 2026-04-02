package com.ProjetoPcamargo.projeto_exemplo.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.UUID;

public interface GenericController {

    // gerando um metodo generico para traçar a rota da requisição rest web
    default URI gerarHeaderLocation(UUID id){
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

    }


}
