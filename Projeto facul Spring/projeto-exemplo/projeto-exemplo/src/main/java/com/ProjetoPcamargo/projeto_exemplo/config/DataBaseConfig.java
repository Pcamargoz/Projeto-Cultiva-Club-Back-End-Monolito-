package com.ProjetoPcamargo.projeto_exemplo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


// por ser uma anotação de configuração
// o spring ja detecta a config que fazemos aqui
@Configuration
public class DataBaseConfig {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;

    // personalizando a conexão com o banco de dados
    @Bean
    public DataSource hikariDataSource(){
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setJdbcUrl(url);
        config.setPassword(password);
        config.setDriverClassName(driver);

        // maximo de conexões liberadas
        config.setMaximumPoolSize(10);

        config.setMinimumIdle(1);// tamanho inicial pool
        config.setPoolName("Library-db-pool");
        config.setMaxLifetime(600000);// maximo de tempo que a conexão vai durar
        config.setConnectionTimeout(100000);// timeout para conseguir uma conexão

        // vai testar se o banco esta funcionando
        // fazendo o teste de select 1 para ver se ele retorna alguma coisa
        config.setConnectionTestQuery("select 1");
        return new HikariDataSource(config);
    }

}
