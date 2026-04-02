package com.example.libraryapi.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String login;

    @Column
    private String senha;

    @Column
    private String email;

    // para traduzirmos essa lista para uma array no banco de dados precisamos adicionar uma dependencia
    //hypersistence , porem antes devemos verificar a versão do nosso hibernate
    // aonde dizemos o tipo dessa coluna la no banco de dados
    // o type faz a tradução da lista para array
    @Type(ListArrayType.class)
    @Column(name = "roles", columnDefinition = "varchar[]")
    private List<String> roles;



}

