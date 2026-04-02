package com.ProjetoPcamargo.projeto_exemplo.controller;

import com.ProjetoPcamargo.projeto_exemplo.controller.DTO.ErroRespostas;
import com.ProjetoPcamargo.projeto_exemplo.controller.DTO.UsuarioDTO;
import com.ProjetoPcamargo.projeto_exemplo.controller.Mappers.UsuarioMapper;
import com.ProjetoPcamargo.projeto_exemplo.exceptions.RecursoNaoEcontrado;
import com.ProjetoPcamargo.projeto_exemplo.model.ClasseUsuario;
import com.ProjetoPcamargo.projeto_exemplo.model.Usuario;
import com.ProjetoPcamargo.projeto_exemplo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements GenericController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody UsuarioDTO dto){

        var usuario = mapper.toEntity(dto);

        service.salvar(usuario);
        URI location = gerarHeaderLocation(usuario.getId());

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable String id){

        var idUsuario = UUID.fromString(id);
        Optional<Usuario> usuarioOptional = service.obterPorId(idUsuario);

        if (usuarioOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        service.deletar(idUsuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtropesquisausuario")
    public ResponseEntity<List<Usuario>> pesquisarPersonalizada(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "tipo_cliente", required = false) ClasseUsuario tipoDeUsuario){

        List<Usuario> pesquisa = service.pesquisaPersonalizada(nome, tipoDeUsuario, email);
        return ResponseEntity.ok(pesquisa);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> obterPorId(@PathVariable String id) {

        var idUsuario = UUID.fromString(id);
        Optional<Usuario> usuarioOptional = service.obterPorId(idUsuario);

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuarioOptional.get());
    }
    @GetMapping
    public ResponseEntity<List<Usuario>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "tipoDoCliente", required = false) ClasseUsuario tipoDoCliente){

        return ResponseEntity.ok(service.pesquisaByExample(nome, tipoDoCliente));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizarUsuario(@PathVariable String id,
                                                   @RequestBody UsuarioDTO dto){

        var idUsuario = UUID.fromString(id);
        Optional<Usuario> usuarioOptional = service.obterPorId(idUsuario);

        if(usuarioOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var usuarioEncontrado = usuarioOptional.get();
        usuarioEncontrado.setNome(dto.nome());
        usuarioEncontrado.setEmail(dto.email());
        usuarioEncontrado.setMoedas(dto.moedas());
        usuarioEncontrado.setTipoCliente(dto.tipoCliente());

        service.atualizar(usuarioEncontrado);

        return ResponseEntity.noContent().build();
    }
}