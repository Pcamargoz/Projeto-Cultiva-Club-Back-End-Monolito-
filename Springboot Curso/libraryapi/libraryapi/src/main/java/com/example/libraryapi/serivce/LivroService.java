package com.example.libraryapi.serivce;

import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.repository.LivroRepository;
import com.example.libraryapi.repository.specs.LivroSpecs;
import com.example.libraryapi.security.SecurityService;
import com.example.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static com.example.libraryapi.repository.specs.LivroSpecs.*;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository Repository;
    private final LivroValidator validator;
    private final SecurityService securityService;

    public Livro salvar(Livro livro){
        validator.validar(livro);
        Usuario usuario = securityService.obterUsuarioLogado();
        livro.setUsuario(usuario);
        return Repository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id){
        return Repository.findById(id);
    }
    public void Deletar(Livro livro){
        Repository.delete(livro);
    }

    // por ser do tipo page no responseentity a gente tem que retornar o objeto e a nossa pagerequest
    public Page<Livro> pesquisa(String isbn, String titulo, String nomeAutor,
                         GeneroLivro genero, Integer anoPublicacao, Integer pagina,
                         Integer tamanhPagina){

        // select * from livro where isbn = :isbn and nomeAutor = / e assim por diante conforme ele pufor passando vai pesquisando
       // Specification<Livro> specs = Specification.where(
        //        LivroSpecs.isbnEqual(isbn))
        //      .and(LivroSpecs.tituloLike(titulo))
        //    .and(LivroSpecs.generoEqual(genero));


        // select * from livro where 0 = 0
        // fazer os if caso ele não preeencha todos os campos
        Specification<Livro> specs = Specification.where(((root, query, cb) -> cb.conjunction()));
        // if com os metodos staticos que precisam ser importados para caso todos os campos nao sejam preenchidos
        if(isbn != null){
            specs = specs.and(isbnEqual(isbn));
        }
        if(titulo != null){
            specs = specs.and(tituloLike(titulo));
        }
        if(genero != null){
            specs = specs.and(generoEqual(genero));
        }
        if(anoPublicacao != null){

            specs = specs.and(anoPublicacaoEqual(anoPublicacao));
        }
        if(nomeAutor != null){

            specs = specs.and(nomeAutorLike(nomeAutor));
        }

        // tomar cuidado com os imports pq pode fazer diferença e por isso da erro
        Pageable pageRequest = PageRequest.of(pagina,tamanhPagina);

        return Repository.findAll(specs, pageRequest);
    }
    public void atualizar(Livro livro){
        if(livro.getId() == null){
            throw new IllegalArgumentException("Para atualizar, é necessário que o livro ja esteja salvado na base ");
        }
        validator.validar(livro);
        Repository.save(livro);
    }

}
