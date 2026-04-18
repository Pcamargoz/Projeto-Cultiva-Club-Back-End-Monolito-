package com.cultivaclub.monolito.objetos.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Tarefas", schema = "public")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Tarefas {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "concluida", nullable = false)
    private Boolean concluida = false;

    @Column(name = "data_limite")
    private LocalDateTime dataLimite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Cards card;

    @CreatedDate
    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
}
