package com.ProjetoPcamargo.projeto_exemplo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    // dizendo o tipo de enum que vamos passar ao inserir
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", length = 30, nullable = false)
    private ClasseUsuario tipoCliente;

    @Column(name = "moedas", length = 18, scale = 2)
    private BigDecimal moedas;

    // um para muitos
    // não e uma boa pratica mas nesse caso faz sentido
    // Lazy = Não carregue os dados relacionados agora. Só busque quando alguém realmente usar.
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contrato> contratos;

    @CreatedDate
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;


}
