package com.example.libraryapi.security;

import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.serivce.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UsuarioService usuarioService;

    // toda vez que precisar do usuario logado podemos usar isso aqui tbm
    // isso aqui esta sendo aplicado no service não no controller
    public Usuario obterUsuarioLogado(){
        // dentro do contexto do spring security ele esta pegando o objeto authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        /*
        verifica se authentication é um CustomAuthentication
        se for, permite fazer cast para usar os métodos da classe.
         */

        if(authentication instanceof CustomAuthentication customAuth){
            return customAuth.getUsuario();
        }
        return null;
    }
}
