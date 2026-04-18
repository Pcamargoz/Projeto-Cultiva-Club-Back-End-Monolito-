package com.cultivaclub.monolito.objetos.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Cards", schema = "public")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Cards {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "alimento", length = 100, nullable = false)
    private TIPO_ALIMENTOS alimentos;

    @Column(name = "id_usuario", nullable = false)
    private UUID id_usuario;

    @Column(name = "anotacoes", length = 1000, nullable = false)
    private String anotacoes;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Tarefas> tarefas = new ArrayList<>();

    @CreatedDate
    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
