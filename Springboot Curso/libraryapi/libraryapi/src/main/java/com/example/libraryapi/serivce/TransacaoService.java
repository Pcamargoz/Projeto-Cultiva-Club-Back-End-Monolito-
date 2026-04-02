package com.example.libraryapi.serivce;

import com.example.libraryapi.model.Autor;
import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.repository.AutorRepository;
import com.example.libraryapi.repository.LivroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public void atualizacaoSemAtualizar(){
        // aqui no caso nao precisamos de save por conta da transação
        // que ja commita as alterações
        var livro = livroRepository.findById(UUID.fromString(""))
                .orElse(null);

        livro.setDataPublicacao(LocalDate.of(2024,6,1));
    }

    @Transactional
    // so funciona com public
    public void executar(){

        // salva o autor
        // estado transient
        Autor autor = new Autor();
        autor.setNome("Fransisca");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1951,1,31));
        autorRepository.save(autor);


        // salva livro
        // no caso da associação , quando criamos o livro a gente ja associa o livro ao autor pelo id
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Livro Da Fransisca");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));
        livro.setAutor(autor);

        // ele ja manda diretamente para o banco a operação
        // porem mesmo se der erro o rollback e feito
        livroRepository.saveAndFlush(livro);

        // forçando erro
        if(autor.getNome().equals("Fransisca")){
            throw new RuntimeException("Rollback");
        }

    }

    //exemplo

    // livro (titulo,...., nome_arquivo) -> id.png
    @Transactional
    public void salvarLivroComFoto(){
        //salva o livro
        // repository.save(livro)

        // pega o id do livro = livro.getId();
        // var id = livro.getId();

        // salvar foto do livro -> bucket na nuvem
        // bucketservice.salvar(livro.getFoto(), id + ".png");

        // atualizar o nome arquivo que foi salvo
        // livro.setNomeArquivoFoto(id + ".png");

        // nao preciso chamar o save pq como e transação ele salva no commit
    }
}
