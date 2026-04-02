package com.example.libraryapi.controller.mappers;

import com.example.libraryapi.controller.dto.AutorDTO;
import com.example.libraryapi.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

// vai tranformar isso aqui em um componet
@Mapper(
        componentModel = "spring"
)
public interface AutorMapper {

    // gera codigo para mapear as entidades de DTO para entidade real
    // precisamos apens usar o mapping para variaveis que sao praticamente a mesma coisa porem possuem nomes diferentes
    @Mapping(source = "nome", target = "nome")
    Autor toEntity(AutorDTO dto);

    AutorDTO toDTO(Autor autor);
}
