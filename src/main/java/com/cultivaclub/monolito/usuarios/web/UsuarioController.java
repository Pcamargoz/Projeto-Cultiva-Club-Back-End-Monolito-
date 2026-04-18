package com.cultivaclub.monolito.usuarios.web;

import com.cultivaclub.monolito.usuarios.domain.Usuario;
import com.cultivaclub.monolito.usuarios.service.UsuarioService;
import com.cultivaclub.monolito.usuarios.web.dto.AtualizarRoleDTO;
import com.cultivaclub.monolito.usuarios.web.dto.ResultadoPesquisaDeUsuarioDTO;
import com.cultivaclub.monolito.usuarios.web.dto.UsuarioDTO;
import com.cultivaclub.monolito.usuarios.web.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cadastro")
@RequiredArgsConstructor
@Validated
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @PostMapping
    public ResponseEntity<ResultadoPesquisaDeUsuarioDTO> criar(@RequestBody @Valid UsuarioDTO dto) {
        Usuario usuario = mapper.toEntity(dto);
        Usuario salvo = service.salvar(usuario);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(mapper.toDTO(salvo));
    }

    @GetMapping
    public ResponseEntity<List<ResultadoPesquisaDeUsuarioDTO>> listarTodos() {
        List<ResultadoPesquisaDeUsuarioDTO> usuarios = service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoPesquisaDeUsuarioDTO> buscarPorId(@PathVariable UUID id) {
        return service.obterPorId(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultadoPesquisaDeUsuarioDTO> atualizar(@PathVariable UUID id,
                                                                    @RequestBody @Valid UsuarioDTO dto) {
        return service.obterPorId(id)
                .map(existente -> {
                    existente.setNome(dto.nome());
                    existente.setLogin(dto.login());
                    existente.setEmail(dto.email());
                    boolean senhaAlterada = !service.senhaConfere(dto.senha(), existente.getSenha());
                    Usuario salvo;
                    if (senhaAlterada) {
                        existente.setSenha(dto.senha());
                        salvo = service.atualizar(existente);
                    } else {
                        salvo = service.atualizarSemReencodarSenha(existente);
                    }
                    return ResponseEntity.ok(mapper.toDTO(salvo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<ResultadoPesquisaDeUsuarioDTO> atualizarRole(@PathVariable UUID id,
                                                                       @RequestBody @Valid AtualizarRoleDTO dto) {
        Usuario atualizado = service.atualizarRole(id, dto.role());
        return ResponseEntity.ok(mapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (service.obterPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
