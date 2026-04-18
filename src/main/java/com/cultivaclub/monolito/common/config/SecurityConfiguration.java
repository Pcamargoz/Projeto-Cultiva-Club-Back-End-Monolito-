package com.cultivaclub.monolito.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança única do monólito.
 *
 * Unifica as regras antes separadas entre MS-1 (rota /cadastro, /login, BCrypt)
 * e MS-2 (rotas /api/cards). Endpoints públicos por enquanto até a introdução
 * da autenticação JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/cadastro", "/cadastro/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login", "/login/").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/cards/**").permitAll()
                        .anyRequest().permitAll()
                )
                .build();
    }
}
