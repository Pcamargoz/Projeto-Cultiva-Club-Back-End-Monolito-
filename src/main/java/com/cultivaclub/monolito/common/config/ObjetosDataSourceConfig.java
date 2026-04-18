package com.cultivaclub.monolito.common.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuração do DataSource secundário: database de objetos (Cards + Tarefas).
 *
 * Escopo:
 *   - Entidades : {@code com.cultivaclub.monolito.objetos.domain}
 *   - Repos     : {@code com.cultivaclub.monolito.objetos.repository}
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.cultivaclub.monolito.objetos.repository",
        entityManagerFactoryRef = "objetosEntityManagerFactory",
        transactionManagerRef = "objetosTransactionManager"
)
public class ObjetosDataSourceConfig {

    @Bean
    @ConfigurationProperties("app.datasources.objetos")
    public DataSourceProperties objetosDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasources.objetos.hikari")
    public HikariDataSource objetosDataSource(
            @Qualifier("objetosDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean objetosEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("objetosDataSource") DataSource dataSource,
            JpaProperties jpaProperties) {

        Map<String, Object> props = new HashMap<>(jpaProperties.getProperties());
        return builder
                .dataSource(dataSource)
                .packages("com.cultivaclub.monolito.objetos.domain")
                .persistenceUnit("objetosPU")
                .properties(props)
                .build();
    }

    @Bean
    public PlatformTransactionManager objetosTransactionManager(
            @Qualifier("objetosEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
