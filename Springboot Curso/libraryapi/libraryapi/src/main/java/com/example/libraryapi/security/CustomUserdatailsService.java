package com.example.libraryapi.security;

import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.serivce.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserdatailsService implements UserDetailsService{

    private final UsuarioService service;


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = service.obterPorLogin(login);

        if(usuario == null){
            throw new UsernameNotFoundException("Usuario Não Encontrado ! ");
        }
        // servindo para que a api consiga enxergar cada dado mencionado
        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                // transformando uma lista de string em uma array
                .roles(usuario.getRoles().toArray(usuario.getRoles().toArray(new String[usuario.getRoles().size()])))
                .build();
    }
}
