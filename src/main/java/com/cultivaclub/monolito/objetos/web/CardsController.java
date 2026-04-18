package com.cultivaclub.monolito.objetos.web;

import com.cultivaclub.monolito.objetos.domain.TIPO_ALIMENTOS;
import com.cultivaclub.monolito.objetos.service.CardsService;
import com.cultivaclub.monolito.objetos.web.dto.CardDTO;
import com.cultivaclub.monolito.objetos.web.dto.CardRespostaDTO;
import com.cultivaclub.monolito.objetos.web.dto.ResultadoDePesquisaCardsDTO;
import com.cultivaclub.monolito.objetos.web.dto.TarefaDTO;
import com.cultivaclub.monolito.objetos.web.dto.TarefaRespostaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api/cards", "/api/cards/"})
@RequiredArgsConstructor
public class CardsController {

    private final CardsService cardsService;

    @PostMapping
    public ResponseEntity<CardRespostaDTO> criar(@RequestBody @Valid CardDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardsService.criarCard(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardRespostaDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(cardsService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<ResultadoDePesquisaCardsDTO> pesquisar(
            @RequestParam UUID idUsuario,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) TIPO_ALIMENTOS alimento,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanhoPagina) {
        return ResponseEntity.ok(cardsService.pesquisar(idUsuario, titulo, alimento, pagina, tamanhoPagina));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardRespostaDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid CardDTO dto) {
        return ResponseEntity.ok(cardsService.atualizarCard(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id, @RequestParam UUID idUsuario) {
        cardsService.deletarCard(id, idUsuario);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cardId}/tarefas")
    public ResponseEntity<TarefaRespostaDTO> criarTarefa(
            @PathVariable UUID cardId, @RequestBody @Valid TarefaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardsService.criarTarefa(cardId, dto));
    }

    @PutMapping("/{cardId}/tarefas/{tarefaId}")
    public ResponseEntity<TarefaRespostaDTO> atualizarTarefa(
            @PathVariable UUID cardId, @PathVariable UUID tarefaId, @RequestBody @Valid TarefaDTO dto) {
        return ResponseEntity.ok(cardsService.atualizarTarefa(cardId, tarefaId, dto));
    }

    @DeleteMapping("/{cardId}/tarefas/{tarefaId}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable UUID cardId, @PathVariable UUID tarefaId) {
        cardsService.deletarTarefa(cardId, tarefaId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cardId}/tarefas/{tarefaId}/concluir")
    public ResponseEntity<TarefaRespostaDTO> concluirTarefa(
            @PathVariable UUID cardId, @PathVariable UUID tarefaId) {
        return ResponseEntity.ok(cardsService.concluirTarefa(cardId, tarefaId));
    }

    @GetMapping("/tarefas/calendario")
    public ResponseEntity<List<TarefaRespostaDTO>> calendario(
            @RequestParam UUID idUsuario, @RequestParam LocalDate data) {
        return ResponseEntity.ok(cardsService.calendarioPorData(idUsuario, data));
    }
}
