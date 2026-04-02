package com.ProjetoPcamargo.projeto_exemplo.controller.Mappers;

import com.ProjetoPcamargo.projeto_exemplo.controller.DTO.UsuarioDTO;
import com.ProjetoPcamargo.projeto_exemplo.model.Usuario;
import com.ProjetoPcamargo.projeto_exemplo.repository.UsuarioRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class UsuarioMapper {
    @Autowired
    UsuarioRepository usuarioRepository;

    // por nao ter nenhuma exceção nao precisamos colocar o mapping e colocar exceções a mais
    public abstract Usuario toEntity(UsuarioDTO dto);



}
