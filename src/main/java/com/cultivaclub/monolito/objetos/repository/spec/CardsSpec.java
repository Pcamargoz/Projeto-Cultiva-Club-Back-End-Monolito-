package com.cultivaclub.monolito.objetos.repository.spec;

import com.cultivaclub.monolito.objetos.domain.Cards;
import com.cultivaclub.monolito.objetos.domain.TIPO_ALIMENTOS;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

public class CardsSpec {

    private CardsSpec() {
        // Utility class - no instantiation
    }

    public static Specification<Cards> tituloLike(String titulo) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%");
    }

    public static Specification<Cards> alimentoEqual(TIPO_ALIMENTOS alimento) {
        return (root, query, cb) ->
                cb.equal(root.get("alimentos"), alimento);
    }

    public static Specification<Cards> anotacoesLike(String anotacoes) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("anotacoes")), "%" + anotacoes.toUpperCase() + "%");
    }

    public static Specification<Cards> dataCadastroEntre(LocalDateTime inicio, LocalDateTime fim) {
        return (root, query, cb) ->
                cb.between(root.get("dataCadastro"), inicio, fim);
    }

    public static Specification<Cards> idUsuarioEqual(UUID idUsuario) {
        return (root, query, cb) ->
                cb.equal(root.get("id_usuario"), idUsuario);
    }
}
