package com.ProjetoPcamargo.projeto_exemplo.controller;

import com.ProjetoPcamargo.projeto_exemplo.controller.DTO.ErroRespostas;
import com.ProjetoPcamargo.projeto_exemplo.exceptions.OperacaoNaoPermitida;
import com.ProjetoPcamargo.projeto_exemplo.model.Contrato;
import com.ProjetoPcamargo.projeto_exemplo.model.Usuario;
import com.ProjetoPcamargo.projeto_exemplo.service.AssociacaoService;
import com.ProjetoPcamargo.projeto_exemplo.service.ContratoService;
import com.ProjetoPcamargo.projeto_exemplo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
// falando qual uri ele vai escutar
@RequestMapping("/associacao/contratos")
//
@RequiredArgsConstructor
public class AssociacaoContratos implements GenericController {
    private final ContratoService contratoService;
    private final UsuarioService usuarioService;
    private final AssociacaoService associacaoService;



    @PostMapping
    public ResponseEntity<Object> associar(@RequestParam String id,
                                           @RequestParam String id2) {
            UUID idContrato = UUID.fromString(id);
            UUID idUsuario = UUID.fromString(id2);

            Optional<Contrato> contratoOptional = contratoService.obterPorId(idContrato);
            Optional<Usuario> usuarioOptional = usuarioService.obterPorId(idUsuario);

            // dispara um erro para caso não achar o contrato ou o usuario
            if (contratoOptional.isEmpty() || usuarioOptional.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Usuário ou contrato não encontrado");
            }

            associacaoService.associar(
                    usuarioOptional.get(),
                    contratoOptional.get()
            );

            URI location = gerarHeaderLocation(contratoOptional.get().getId());

            return ResponseEntity.created(location).build();
    }
}
