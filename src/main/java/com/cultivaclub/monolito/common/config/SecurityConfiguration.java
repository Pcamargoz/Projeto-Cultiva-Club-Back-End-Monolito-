package com.cultivaclub.monolito.common.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança única do monólito.
 *
 * Unifica as regras antes separadas entre MS-1 (rota /cadastro, /login, BCrypt)
 * e MS-2 (rotas /api/cards). Endpoints públicos por enquanto até a introdução
 * da autenticação JWT. O OAuth2 login via Google é habilitado somente quando
 * o bean {@link ClientRegistrationRepository} está presente (vide
 * GoogleOAuth2Config).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ObjectProvider<ClientRegistrationRepository> clientRegistrationRepository,
            ObjectProvider<OAuth2LoginSuccessHandler> oAuth2LoginSuccessHandler
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/cadastro", "/cadastro/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login", "/login/").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/cards/**").permitAll()
                        .anyRequest().permitAll()
                );

        if (clientRegistrationRepository.getIfAvailable() != null) {
            OAuth2LoginSuccessHandler handler = oAuth2LoginSuccessHandler.getIfAvailable();
            http.oauth2Login(oauth -> {
                if (handler != null) {
                    oauth.successHandler(handler);
                }
            });
        }

        return http.build();
    }
}
