package com.cultivaclub.monolito.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CORS do monólito.
 *
 * {@code app.cors.frontend-url} aceita UMA ou VÁRIAS origens, separadas por
 * vírgula — ex.:
 *   FRONTEND_URL=http://localhost:3000,https://cultivaclub.vercel.app
 */
@Configuration
public class CorsConfig {

    @Value("${app.cors.frontend-url:http://localhost:3000}")
    private String frontendUrls;

    @Bean
    public CorsFilter corsFilter() {
        List<String> origins = Arrays.stream(frontendUrls.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(origins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
