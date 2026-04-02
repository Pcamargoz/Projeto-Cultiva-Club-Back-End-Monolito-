package com.example.libraryapi.security;

import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.serivce.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// classe que vai authenticar nossos usuarios
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UsuarioService service;
    private final PasswordEncoder encoder;

    // metodo que vai authenticar
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        // pode ser qualquer tipo de senha , facial , digital e etc
        String senhaDigitada = authentication.getCredentials().toString();

        Usuario usuarioEncontrado = service.obterPorLogin(login);
        if(usuarioEncontrado == null){
            throw getErroUsuarioNaoEncontrado();
        }

        // pegando a senha do usuario encontrado do banco
        String senhaCriptografada = usuarioEncontrado.getSenha();

        // vai verificar se a senha digitada bate com a criptografada no banco
        // e retornando um boolean para que a gente faça o if
        boolean senhasBatem = encoder.matches(senhaDigitada, senhaCriptografada);
        if(senhasBatem){
            return new CustomAuthentication(usuarioEncontrado);
        }

        throw getErroUsuarioNaoEncontrado();
    }

    private UsernameNotFoundException getErroUsuarioNaoEncontrado(){
        return new UsernameNotFoundException("Usuario ou senha incorretos !");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // ele vai criar um provider a partir de login e senha e vai dizer qual clase que eleaceita
        // esse objeto que estamos criando aqui que vai ser retornado como authenticação para os outros metodos
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
