package com.cultivaclub.monolito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point do monólito CultivaClub.
 *
 * NOTA: {@code @EnableJpaAuditing} não está aqui porque os @CreatedDate /
 * @LastModifiedDate são habilitados dentro de cada DataSourceConfig
 * (vide UsuariosDataSourceConfig). Isso evita conflito entre os dois
 * EntityManagerFactory do setup multi-database.
 */
@SpringBootApplication
public class CultivaClubApplication {
    public static void main(String[] args) {
        SpringApplication.run(CultivaClubApplication.class, args);
    }
}
