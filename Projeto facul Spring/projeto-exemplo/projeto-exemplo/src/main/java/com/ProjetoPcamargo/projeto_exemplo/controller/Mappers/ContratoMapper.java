package com.ProjetoPcamargo.projeto_exemplo.controller.Mappers;

import com.ProjetoPcamargo.projeto_exemplo.controller.DTO.ContratoDTO;
import com.ProjetoPcamargo.projeto_exemplo.model.Contrato;
import com.ProjetoPcamargo.projeto_exemplo.repository.UsuarioRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ContratoMapper {

    @Autowired
    UsuarioRepository usuarioRepository;

    /*@Mapping(
            target = "usuario",
            expression = "java(usuarioRepository.findById(dto.idUsuario()).orElse(null))"
    )*/
    public abstract Contrato toEntity(ContratoDTO dto);
}