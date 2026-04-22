package com.cultivaclub.monolito.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

/**
 * Registra o provider Google para OAuth2 somente quando GOOGLE_CLIENT_ID
 * e GOOGLE_CLIENT_SECRET estão definidos. Isto evita falha de inicialização
 * em ambientes onde o login social não está configurado (ex.: containers
 * sem as variáveis de ambiente).
 */
@Configuration
@ConditionalOnProperty(name = "GOOGLE_CLIENT_ID")
public class GoogleOAuth2Config {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(
            @Value("${GOOGLE_CLIENT_ID}") String clientId,
            @Value("${GOOGLE_CLIENT_SECRET}") String clientSecret
    ) {
        ClientRegistration google = CommonOAuth2Provider.GOOGLE
                .getBuilder("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .build();
        return new InMemoryClientRegistrationRepository(google);
    }
}
