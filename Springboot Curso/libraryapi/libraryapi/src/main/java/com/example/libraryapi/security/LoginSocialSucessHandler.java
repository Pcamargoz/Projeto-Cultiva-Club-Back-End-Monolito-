package com.example.libraryapi.security;

import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.serivce.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// serve tbm para entrar no contexto do spring
@Component
@RequiredArgsConstructor
// o extends vai server para a gente trabalhar com a authenticação que vamos receber do oauth2
public class LoginSocialSucessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UsuarioService Usuarioservice;

    /*
    Esse método está tratando o sucesso da autenticação OAuth2 e,
    a partir do objeto Authentication, você consegue extrair informações do usuário autenticado, como o email, nome, foto, etc.
     */

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        // recebendo o token do auth2 em authetication
        OAuth2AuthenticationToken auth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        // pegando o principal dele
        OAuth2User oAuth2User = auth2AuthenticationToken.getPrincipal();
        // pegando o atributo que queremos atraves de string
        String email = oAuth2User.getAttribute("email");

        // como a gente cadastrou um usuario com o email do google , ele esta puxando o nome do usuario com base no email do google
        Usuario usuario = Usuarioservice.obterPorEmail(email);

        //transformando a authentication em custom pra gente conseguir trabalhar com o "Repositorio
        CustomAuthentication customAuthentication = new CustomAuthentication(usuario);

        // pegando o contexto e aplicando na nossa custom
        SecurityContextHolder.getContext().setAuthentication(customAuthentication);

        // para a gente pode ver como vamos chegar aonde queremos
        // como queremos o email do usuario
        //System.out.println(email);

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
