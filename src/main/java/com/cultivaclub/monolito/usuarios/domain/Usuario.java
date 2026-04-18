package com.cultivaclub.monolito.usuarios.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Usuario", schema = "public")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "login", length = 50, nullable = false, unique = true)
    private String login;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "senha", length = 255, nullable = false)
    private String senha;

    @Column(name = "nome", length = 150)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private ROLES role = ROLES.Usuario;

    @CreatedDate
    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
