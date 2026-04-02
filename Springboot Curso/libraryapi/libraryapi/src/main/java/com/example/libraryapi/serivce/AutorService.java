package com.example.libraryapi.serivce;

import com.example.libraryapi.exceptions.OperacaoNaoPermitida;
import com.example.libraryapi.model.Autor;
import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.repository.AutorRepository;
import com.example.libraryapi.repository.LivroRepository;
import com.example.libraryapi.security.SecurityService;
import com.example.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
// ele gera o construtor que eu ia ter
@RequiredArgsConstructor
public class AutorService {


    private final AutorRepository repository;
    private final AutorValidator validator;
    private final LivroRepository livroRepository;
    private final SecurityService securityService;


    //

    public Autor salvar(Autor autor){
        validator.validar(autor);
        // isso aqui esta sendo aplicado no service não no controller
        Usuario usuario = securityService.obterUsuarioLogado();
        autor.setUsuario(usuario);
        return repository.save(autor);
    }

    public void Atualizar(Autor autor){
        if(autor == null){
            throw new IllegalArgumentException("Para Atualizar e nescessario que o autor esteja cadastrado");
        }
        validator.validar(autor);
        repository.save(autor);
    }

    public Optional<Autor> obterPorId(UUID id){
        return repository.findById(id);
    }

    public void deletar(Autor autor){
        if(possuilivro(autor)){
            throw new OperacaoNaoPermitida("Esse autor possui Livro");
        }
        repository.delete(autor);
    }


    public List<Autor> pesquisa(String nome, String nacionalidade){
        // vai mostrar autores com o nome e nacionalidade
        if(nome != null && nacionalidade != null){
            return repository.findByNomeAndNacionalidade(nome, nacionalidade);
        }
        // autores com o mesmo nome
        if(nome != null){
            return repository.findByNome(nome);
        }
        // autores com a nacionalidade
        if(nacionalidade != null){
            return repository.findByNacionalidade(nacionalidade);
        }
        // se nao passar nenhum retorna todos

        return repository.findAll();
    }

    public List<Autor> pesquisaByExample(String nome, String nacionalidade){
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                //.withIgnorePaths("") ele vai ignorar os campos que eu colocar ali
                .withIgnoreCase()
                // qualquer coisa que conter o que a gente esta falando ele vai puxar
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        // o example .of ele precisa de um objeto que criamos e um matcher que setamos com
        // suas dependencias que criamos
        Example<Autor> autorExample = Example.of(autor,matcher);

        // vai retornar todos que o example achar
        return repository.findAll(autorExample);
    }

    // usa para sabermos se o autor possui um livro
    public boolean possuilivro(Autor autor){
        return livroRepository.existsByAutor(autor);
    }

}
