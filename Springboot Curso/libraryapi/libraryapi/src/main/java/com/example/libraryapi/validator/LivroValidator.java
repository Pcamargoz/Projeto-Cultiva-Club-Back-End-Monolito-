package com.example.libraryapi.validator;

import com.example.libraryapi.exceptions.CampoInvalidoException;
import com.example.libraryapi.exceptions.RegistroDuplicadoException;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.repository.LivroRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {

    private final LivroRepository repository;
    // aqui estamos colocando uma variavel statica para uso
    private static final int ANO_EXIGENCIA_PRECO = 2020;

    public void validar(Livro livro){
        if(existeLivroComISBN(livro)){
            throw new RegistroDuplicadoException("ISBN ja cadastrado ! ");
        }
        if(isPrecoObrigatorio(livro)){
            // pegando o erro que criamos para colocar nos mesmo o campo e a mensagem
            throw new CampoInvalidoException("preco", "Para livros com ano de publicação a partir de 2020, o preço e obrigatorio");
        }
    }

    private boolean isPrecoObrigatorio(Livro livro) {
        return livro.getPreco() == null &&
                // pegando o ano
                livro.getDataPublicacao().getYear() >= ANO_EXIGENCIA_PRECO;

    }

    private boolean existeLivroComISBN(Livro livro){
        Optional<Livro> livroEncontrado = repository.findByIsbn(livro.getIsbn());
        if(livro.getId() == null){
            return livroEncontrado.isPresent();
        }
        // caso o id do livro que eu estou cadastrando for diferente do id que for encontrado
        return livroEncontrado.map(Livro::getId)
                .stream()
                .anyMatch(id -> !id.equals(livro.getId()));
    }

}
