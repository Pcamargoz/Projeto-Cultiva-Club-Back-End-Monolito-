package com.example.libraryapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// agora consigo usar paginas web dentro da minha aplicação
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    // registrando a pagina web
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        // MAIS ALTA PRECEDENCIA NA APLICAÇÃO
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}
