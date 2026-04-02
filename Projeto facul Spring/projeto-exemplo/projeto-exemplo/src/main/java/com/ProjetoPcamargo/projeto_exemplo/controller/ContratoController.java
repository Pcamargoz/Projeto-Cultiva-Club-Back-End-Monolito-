package com.ProjetoPcamargo.projeto_exemplo.controller;

import com.ProjetoPcamargo.projeto_exemplo.controller.DTO.ContratoDTO;
import com.ProjetoPcamargo.projeto_exemplo.controller.Mappers.ContratoMapper;
import com.ProjetoPcamargo.projeto_exemplo.model.Contrato;
import com.ProjetoPcamargo.projeto_exemplo.service.ContratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/contratos")
@RequiredArgsConstructor
public class ContratoController implements GenericController {

    private final ContratoService service;
    private final ContratoMapper mapper;

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody ContratoDTO contrato) {

        Contrato contratoDto = mapper.toEntity(contrato);
        service.salvar(contratoDto);

        URI location = gerarHeaderLocation(contratoDto.getId());

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletarContrato(@PathVariable("id") String id) {

        var idContrato = UUID.fromString(id);
        Optional<Contrato> contratoOptional = service.obterPorId(idContrato);

        if (contratoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deletar(idContrato);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtropesquisacontrato")
    public ResponseEntity<List<Contrato>> pesquisar(
            @RequestParam(value = "fornecedor", required = false) String fornecedor,
            @RequestParam(value = "servico", required = false) String servico,
            @RequestParam(value = "preco", required = false) BigDecimal preco) {

        List<Contrato> lista = service.pesquisaPersonalizada(
                fornecedor, servico, preco);

        return ResponseEntity.ok(lista);
    }
    @GetMapping
    public ResponseEntity<List<Contrato>> pesquisarSimples(
            @RequestParam(value = "fornecedor",required = false) String fornecedor,
            @RequestParam(value = "servico", required = false) String servico
    ){
        return ResponseEntity.ok(service.pesquisaByExample(fornecedor,servico));

    }

    @PutMapping("{id}")
    public ResponseEntity<Object> AtualizarPreco(
            @PathVariable("id") String id,
            @RequestBody ContratoDTO dto) {

        var idContrato = UUID.fromString(id);
        Optional<Contrato> contratoOptional = service.obterPorId(idContrato);

        if (contratoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var contratoA = contratoOptional.get();
        contratoA.setFornecedor(dto.fornecedor());
        contratoA.setServico(dto.servico());
        contratoA.setPreco(dto.preco());

        service.atualizar(contratoA);
        return ResponseEntity.noContent().build();
    }
}