package com.cultivaclub.monolito.usuarios.web.mapper;

import com.cultivaclub.monolito.usuarios.domain.Usuario;
import com.cultivaclub.monolito.usuarios.web.dto.ResultadoPesquisaDeUsuarioDTO;
import com.cultivaclub.monolito.usuarios.web.dto.UsuarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(source = "email", target = "email")
    Usuario toEntity(UsuarioDTO dto);

    @Mapping(source = "role", target = "roles")
    ResultadoPesquisaDeUsuarioDTO toDTO(Usuario usuario);
}
