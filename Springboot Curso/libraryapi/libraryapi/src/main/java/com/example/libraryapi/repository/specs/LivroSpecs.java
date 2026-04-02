package com.example.libraryapi.repository.specs;

import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

// aqui vai ser como a spec que recebemos vai ser "Formatada" para que o repository possa ter
// entendimento do que vai ser procurado la dentro

// regra de pesquisa com spec
public class LivroSpecs {

    // specs referenciadas a objetos da entidade e nao campos do banco de dados
    public static Specification<Livro> isbnEqual (String isbn){
        // como aqui so queremos se o isbn for igual
        return ((root, query, cb) -> cb.equal(root.get("isbn"), isbn));
    }
    public static Specification<Livro> tituloLike(String titulo){
        // upper (livro, titulo) like (%:param%)
        // o upper e muito usado por conta que a api deve ler o que a gente recebe o que procuramos
        // como upper tambem para facilitarmos a pesquisa
        return ((root, query, cb) ->
                cb.like(cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%"));
    }
    public static Specification<Livro> generoEqual (GeneroLivro genero){
        return ((root, query, cb) ->
                cb.equal(root.get("genero"), genero));
    }
    public static Specification<Livro> anoPublicacaoEqual (Integer anoPublicacao){
        // and to_char(data_publicacao, "YYYY") = :anoPublicacao
        return (root, query, cb) ->
                // ta falando a função que ele vai atuar e o tipo de objeto que ele vai receber
                cb.equal(cb.function("to_char", String.class,
                        // vai responder ao objeto dataPubicacao do jeito literal que falamos
                        // e depois a gente so transformamos o ano publicacao que vamos colocar em String
                        root.get("dataPublicacao"),cb.literal("YYYY")), anoPublicacao.toString());

    }
    public static Specification<Livro>  nomeAutorLike (String nome){
        return (root, query, cb) -> {
            // criando um join
            Join<Object,Object> joinAutor = root.join("autor", JoinType.INNER);
            // aplica filtro LIKE no nome do autor após realizar o INNER JOIN,
            // permitindo busca parcial e case-insensitive pelo nome do autor
            // %% = qualquer sequência de caracteres (inclusive nenhuma)
            return cb.like(cb.upper(joinAutor.get("nome")), "%" + nome.toUpperCase() + "%");

            // ele da um get no autor e dentro do autor ele da um get no nome
            //return cb.like(cb.upper(root.get("autor").get("nome")),"%" + nome.toUpperCase() + "%");
        };
    }
}
