package com.example.libraryapi.controller;

import com.example.libraryapi.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// aqui so o controller e quando estou usando paginas web
@Controller
public class LoginViewController {

    // a gente retorna uma string pra falar para qual pagina que ele deve ir dps do login
    @GetMapping("/login")
    public String paginaLogin(){
        // retornando o login html
        return "login";
    }

    // quando voce termina de logar voce vai para a pagina raiz então isso aqui significa a pagina raiz
    @GetMapping("/")
    // ele não vai ter uma view html em cima dessa ação
    @ResponseBody
    public String paginaHome(Authentication authentication){
        if(authentication instanceof CustomAuthentication customAuth){
            System.out.println(customAuth.getUsuario());
        }
        // no caso aqui a gente ainda esta trabalhando com o cadastro que a gente mesmo faz
        return "Ola " + authentication.getName();
    }
}
