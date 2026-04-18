package com.cultivaclub.monolito.common.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.jpa.autoconfigure.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuração do DataSource PRIMÁRIO do monólito: database de usuários.
 *
 * Marcado como {@link Primary} para que beans de infraestrutura que não
 * especificam qualifier continuem funcionando (ex.: Actuator/health).
 *
 * Escopo:
 *   - Entidades : {@code com.cultivaclub.monolito.usuarios.domain}
 *   - Repos     : {@code com.cultivaclub.monolito.usuarios.repository}
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.cultivaclub.monolito.usuarios.repository",
        entityManagerFactoryRef = "usuariosEntityManagerFactory",
        transactionManagerRef = "usuariosTransactionManager"
)
public class UsuariosDataSourceConfig {

    /**
     * Lê url / username / password / driver-class-name de {@code app.datasources.usuarios}.
     */
    @Bean
    @Primary
    @ConfigurationProperties("app.datasources.usuarios")
    public DataSourceProperties usuariosDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Lê as configurações do pool de conexões de {@code app.datasources.usuarios.hikari}.
     * O {@code DataSourceBuilder} cria uma instância de {@link HikariDataSource} e, em seguida,
     * o {@code @ConfigurationProperties} preenche as propriedades do pool (pool-name, max-size, etc.).
     */
    @Bean
    @Primary
    @ConfigurationProperties("app.datasources.usuarios.hikari")
    public HikariDataSource usuariosDataSource(
            @Qualifier("usuariosDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean usuariosEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("usuariosDataSource") DataSource dataSource,
            JpaProperties jpaProperties) {

        Map<String, Object> props = new HashMap<>(jpaProperties.getProperties());
        return builder
                .dataSource(dataSource)
                .packages("com.cultivaclub.monolito.usuarios.domain")
                .persistenceUnit("usuariosPU")
                .properties(props)
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager usuariosTransactionManager(
            @Qualifier("usuariosEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
