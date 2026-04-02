package com.example.libraryapi.controller;

import com.example.libraryapi.controller.dto.UsuarioDTO;
import com.example.libraryapi.controller.mappers.UsuarioMapper;
import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.serivce.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuariosController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid UsuarioDTO dto){
        var usuario = mapper.toEntity(dto);
        service.salvar(usuario);
    }

}
