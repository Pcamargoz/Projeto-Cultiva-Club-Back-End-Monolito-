package com.cultivaclub.monolito.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Habilita o preenchimento automático de {@code @CreatedDate} e
 * {@code @LastModifiedDate} nas entidades (Usuario, Cards, Tarefas).
 *
 * Isolado em sua própria classe porque o setup multi-datasource divide
 * os EntityManagerFactories — o {@code AuditingEntityListener} é registrado
 * globalmente via anotação {@code @EntityListeners} em cada entidade e não
 * precisa ser anexado a um EMF específico.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
