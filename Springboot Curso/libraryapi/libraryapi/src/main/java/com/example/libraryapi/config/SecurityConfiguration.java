package com.example.libraryapi.config;

import com.example.libraryapi.security.CustomUserdatailsService;
import com.example.libraryapi.security.LoginSocialSucessHandler;
import com.example.libraryapi.serivce.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// configurações de segurança
@Configuration
// por ser uma configuração de security
@EnableWebSecurity
// a gente habilita isso aqui para conseguir colocar regras no controller
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    // configuração basica de um spring security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginSocialSucessHandler sucessHandler) throws Exception{
        return http
                // desabilitar o csrf por conta que e uma segurança de pagina web
                // e como nao estamos mexendo com pagina weba ainda , estamos dessabilitando
                .csrf(AbstractHttpConfigurer::disable)

                //.formLogin(configurer -> configurer.loginPage("/login.html").successForwardUrl("home.html"))

                // e como se estivesse configurando nada deixando como padrão configurando nada

                .formLogin(configurer -> {
                    // permitindo que todos acessem a pagina de login
                    configurer.loginPage("/login");
                })

                // aqui estamos usando ele apenas para testar com o login do google se não ele ira puxar o login que nos criamos
                .formLogin(Customizer.withDefaults())

                .httpBasic(Customizer.withDefaults())

                // qualquer requisição ela tem que estar autenticada tanto post tanto no google
                // resumindo precisa ter email e senha
                .authorizeHttpRequests(authorize ->{
                    // so usuarios que tiverem a permissao de admin
                   // authorize.requestMatchers("/autores/**").hasRole("ADMIN");

                    authorize.requestMatchers("/login/**").permitAll();

                    // permitindo que todo mundo consiga fazer um cadastro
                    authorize.requestMatchers(HttpMethod.POST,"/usuarios/**").permitAll();

                    //authorize.requestMatchers("/autores/**").hasRole("ADMIN");
                    //authorize.requestMatchers("/livros/**").hasAnyRole("USER", "ADMIN");

                    authorize.anyRequest().authenticated();


                    // aui esta autorizando uma autoridade, ja roles são grupos que fazem perta de pessoas
                    //authorize.requestMatchers(HttpMethod.POST,"/autores/**").hasAuthority("CADASTRAR_AQUI");
                    //authorize.requestMatchers(HttpMethod.POST,"/autores/**").hasRole("ADMIN");
                    //authorize.requestMatchers(HttpMethod.PUT,"/autores/**").hasRole("ADMIN");
                    //authorize.requestMatchers(HttpMethod.DELETE,"/autores/**").hasRole("ADMIN");

                    // TANTO COMO ADMIN TANTO COMO USUARIO PODE FAZER O GET
                    //authorize.requestMatchers(HttpMethod.GET,"/autores/**").hasAnyRole("ADMIN","USER");
                    // any , ou usario ou admin
                    //authorize.requestMatchers("/livros/**").hasAnyRole("USER", "ADMIN");

                    // se a gente colocar permitall aqui , ele permite todas as requisções porem e bom deixar esse para caso esquecemos de
                    // mapear alguma
                    //authorize.anyRequest().authenticated();

                    // esse tem que ficar por ultimo por que ele so respeita o que esta a cima
                })
                // serve para que a gente trate o login do oauth2 do google
                .oauth2Login(oauth2->{
                        oauth2
                                .loginPage("/login")
                                // recebendo a auhtehnticação de sucesso do google
                                // e tratando ela
                                .successHandler(sucessHandler);
                        })
                .build();

    }
    // cripografia pra senha
    @Bean
    public PasswordEncoder passwordEncoder(){
        // esse metodo uma vez que criptografamos ele nao volta atras
        // passando a força
        return new BCryptPasswordEncoder(10);
    }

    // quando nao temos isso a api gera a senha automaticamente
    // repositorio aonde vai buscar os detalhes dos usuarios
    // @Bean
    // ocultando o bean daqui para o spring não reconhecer e ele pode ler a authenticação que
    // nos criamos
    public UserDetailsService userDetailsService(UsuarioService usuarioService){

        // criando usuario pra teste com dados em memoria
       /*UserDetails user1 = User.builder()
                .username("usuario")
                .password(encoder.encode("123"))
                .roles("USER")
                .build();

        // usando o encoder para ciptografar a senha e a senha ser unica
        UserDetails user2 = User.builder()
                .username("admin")
                .password(encoder.encode("321"))
                .roles("ADMIN")
                .build(); */

        return new CustomUserdatailsService(usuarioService);
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        // aqui passamos o prefixo que queremos
        // no caso aqui a gente não quer nenhum prefixo
        return new GrantedAuthorityDefaults("");
    }
}
