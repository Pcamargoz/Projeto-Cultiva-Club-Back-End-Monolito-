package com.ProjetoPcamargo.projeto_exemplo.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
// para escutar tudo que acontece nessa entidade
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
// gera construtor sem parametros , o construtor vazio
@NoArgsConstructor
// gera construtor com todos os parametros que vamos injetar de outras classes
@AllArgsConstructor
public class Contrato {
    // toda vez que fizermos alguma alteração aqui que envolva a tabela
    // temos que dar um drop na tabela e voltar ela ao normal ou se caso se tiver dados ja
    // temos que retirar o que queremos atraves de querys dentro do banco
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "fornecedor", length = 100, nullable = false)
    private String fornecedor;

    @Column(name = "servico", length = 200, nullable = true)
    private String servico;

    @Column(name = "preco", length = 18, scale = 2, nullable = true)
    private BigDecimal preco;

    // aqui aonde vamos puxar o id do fornecedor
    // muitos para um
    // Lazy = Não carregue os dados relacionados agora. Só busque quando alguém realmente usar.
    @ManyToOne
    @JoinColumn(name = "id_fornecedor")
    private Usuario usuario;

    @CreatedDate
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

}
