package com.example.libraryapi.controller;

import com.example.libraryapi.controller.dto.AutorDTO;
import com.example.libraryapi.controller.mappers.AutorMapper;
import com.example.libraryapi.model.Autor;
import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.serivce.AutorService;
import com.example.libraryapi.serivce.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController implements GeneriController {

    private final AutorService service;
    private final UsuarioService usuarioService;
    private final AutorMapper mapper;

    // =========================
    // SALVAR
    // =========================
    @PostMapping
    // apenas para um role , podendo ser colocado no topo da classe tbm
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO dto,
                                         // serve para a gente fazer a auditoria do usuario que o spring nos fornece
                                         Authentication authentication) {
        // fazendo casting por que ele retorna um objetc porem tem que ser do tipo userdetails
        UserDetails usuarioLogado = (UserDetails) authentication.getPrincipal();
        // com base no principal acima ele esta obtendo as informações para que a gente consiga trabalhar
        Usuario usuario = usuarioService.obterPorLogin(usuarioLogado.getUsername());


        Autor autor = mapper.toEntity(dto);
        autor.setUsuario(usuario);
        service.Atualizar(autor);

        URI location = gerarHeaderLocation(autor.getId());

        return ResponseEntity.created(location).build();
    }

    // =========================
    // OBTER DETALHES
    // =========================
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) {

        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        return autorOptional
                .map(autor -> ResponseEntity.ok(mapper.toDTO(autor)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =========================
    // DELETAR
    // =========================
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {

        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deletar(autorOptional.get());
        return ResponseEntity.noContent().build();
    }

    // =========================
    // PESQUISAR
    // =========================
    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade
    ) {

        List<Autor> resultado = service.pesquisaByExample(nome, nacionalidade);

        List<AutorDTO> lista = resultado.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // =========================
    // ATUALIZAR
    // =========================
    @PutMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id,
            @RequestBody @Valid AutorDTO dto
    ) {

        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Atualização parcial manual (sem mapper)
        var autor = autorOptional.get();
        autor.setNome(dto.nome());
        autor.setNacionalidade(dto.nacionalidade());
        autor.setDataNascimento(dto.dataNascimento());

        service.Atualizar(autor);

        return ResponseEntity.noContent().build();
    }
}