package com.example.libraryapi.security;

import com.example.libraryapi.model.Usuario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// isso aqui e um repositorio de segurança aonde vamos fazer nosso trabalho como se fosse o banco de dados
// porem de segurança
@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {
    // o authenticantion tem que ter todas essas authenticações aqui

    private final Usuario usuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // tranformando as roles em autherities
        return this.usuario
                .getRoles()

                /* aqui estamo dizendo que a ROLE da authenticação que estamos recebendo
                e a role que configuramos no simplegrantedAuthority que nesse caso aqui e nenhuma
                como configuramos de acordo la.
                 */

                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // senha
    @Override
    public Object getCredentials() {
        return null;
    }
    // obter os detalhes por partes
    @Override
    public Object getDetails() {
        return usuario;
    }
    // o principal da nossa authenticação
    @Override
    public Object getPrincipal() {
        return usuario;
    }
    // se tiver false ele não funciona
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }
    // login do usuario
    @Override
    public String getName() {
        return usuario.getLogin();
    }
}
