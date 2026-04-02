package com.example.libraryapi.controller;

import com.example.libraryapi.controller.dto.CadastroLivroDTO;
import com.example.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import com.example.libraryapi.controller.mappers.LivroMapper;
import com.example.libraryapi.model.GeneroLivro;
import com.example.libraryapi.model.Livro;
import com.example.libraryapi.serivce.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


import java.util.UUID;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController implements GeneriController {

    private final LivroService service;
    private final LivroMapper mapper;

    // =========================
    // SALVAR LIVRO
    // =========================
    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto) {

        // Converte DTO → Entidade
        Livro livro = mapper.toEntity(dto);

        // Salva no banco
        service.salvar(livro);

        // Gera header Location
        var url = gerarHeaderLocation(livro.getId());

        return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    // varias roles , podendo ser colocado no topo da classe tbm
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(
            @PathVariable("id") String id){
        /*
         ta achando o livro e esta transformando o livro no dto para que ele possa
         se transformar em um dto para que possamos visualizar
         com todos os mapeamentos de entidades ja setados
         */
        return service.obterPorId(UUID.fromString(id)).map(livro -> {
            var dto = mapper.toDTO(livro);
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> Deletar(@PathVariable("id") String id){

        return service.obterPorId(UUID.fromString(id)).map(livro -> {
            service.Deletar(livro);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.noContent().build() );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam (value = "isbn", required = false)
            String isbn,
            @RequestParam (value = "titulo", required = false)
            String titulo,
            @RequestParam (value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam (value = "genero", required = false)
            GeneroLivro livro,
            @RequestParam (value = "ano-publicacao", required = false)
            Integer anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "tamanha-pagina", defaultValue = "10")
            Integer tamanhoPagina

    ){
        // as validações dos campos se estao vazios ou nao estamos fazendo no service
        var Paginaresultado = service.pesquisa(isbn,titulo,nomeAutor,livro,anoPublicacao,pagina,tamanhoPagina);

        Page<ResultadoPesquisaLivroDTO> resultado =
                Paginaresultado.map(mapper::toDTO);

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<Object> Atualizar(@PathVariable("id") String id,
                                          @RequestBody @Valid CadastroLivroDTO dto){

        return service.obterPorId(UUID.fromString(id))
                // trabalhando com o livro que a gente achou no obter por id no banco
                .map(Livro -> {
                    // transformando em dto
                    Livro EntidadeAux = mapper.toEntity(dto);
                    // atualizando os campos que queremos , no qual estamos pegando do body do dto inserido
                    Livro.setDataPublicacao(EntidadeAux.getDataPublicacao());
                    Livro.setIsbn(EntidadeAux.getIsbn());
                    Livro.setPreco(EntidadeAux.getPreco());
                    Livro.setGenero(EntidadeAux.getGenero());
                    Livro.setAutor(EntidadeAux.getAutor());

                    service.atualizar(Livro);

                    return ResponseEntity.noContent().build();

                    // se não achar vai me retornar isso
                }).orElseGet(() -> ResponseEntity.notFound().build());

    }

}