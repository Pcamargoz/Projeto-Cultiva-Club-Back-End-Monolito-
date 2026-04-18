package com.cultivaclub.monolito.usuarios.web.mapper;

import com.cultivaclub.monolito.usuarios.domain.ROLES;
import com.cultivaclub.monolito.usuarios.domain.Usuario;
import com.cultivaclub.monolito.usuarios.web.dto.ResultadoPesquisaDeUsuarioDTO;
import com.cultivaclub.monolito.usuarios.web.dto.UsuarioDTO;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T11:30:24-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public Usuario toEntity(UsuarioDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setEmail( dto.email() );
        usuario.setLogin( dto.login() );
        usuario.setNome( dto.nome() );
        usuario.setSenha( dto.senha() );

        return usuario;
    }

    @Override
    public ResultadoPesquisaDeUsuarioDTO toDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        ROLES roles = null;
        UUID id = null;
        String login = null;
        String email = null;
        String nome = null;
        LocalDateTime dataCadastro = null;

        roles = usuario.getRole();
        id = usuario.getId();
        login = usuario.getLogin();
        email = usuario.getEmail();
        nome = usuario.getNome();
        dataCadastro = usuario.getDataCadastro();

        ResultadoPesquisaDeUsuarioDTO resultadoPesquisaDeUsuarioDTO = new ResultadoPesquisaDeUsuarioDTO( id, login, email, nome, dataCadastro, roles );

        return resultadoPesquisaDeUsuarioDTO;
    }
}
