package com.cultivaclub.monolito.objetos.service;

import com.cultivaclub.monolito.common.exception.OperacaoNaoPermitida;
import com.cultivaclub.monolito.common.exception.RecursoNaoEncontrado;
import com.cultivaclub.monolito.objetos.domain.Cards;
import com.cultivaclub.monolito.objetos.domain.TIPO_ALIMENTOS;
import com.cultivaclub.monolito.objetos.domain.Tarefas;
import com.cultivaclub.monolito.objetos.repository.CardsRepository;
import com.cultivaclub.monolito.objetos.repository.TarefasRepository;
import com.cultivaclub.monolito.objetos.repository.spec.CardsSpec;
import com.cultivaclub.monolito.objetos.validator.ValidadorCardsPro;
import com.cultivaclub.monolito.objetos.web.dto.CardDTO;
import com.cultivaclub.monolito.objetos.web.dto.CardRespostaDTO;
import com.cultivaclub.monolito.objetos.web.dto.ResultadoDePesquisaCardsDTO;
import com.cultivaclub.monolito.objetos.web.dto.TarefaDTO;
import com.cultivaclub.monolito.objetos.web.dto.TarefaRespostaDTO;
import com.cultivaclub.monolito.objetos.web.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardsService {

    private final CardsRepository cardsRepository;
    private final TarefasRepository tarefasRepository;
    private final CardMapper cardMapper;
    private final ValidadorCardsPro validator;

    @Transactional
    public CardRespostaDTO criarCard(CardDTO dto) {
        validator.validarLimiteCards(dto.idUsuario());
        Cards card = cardMapper.toEntity(dto);
        Cards salvo = cardsRepository.save(card);
        return cardMapper.toRespostaDTO(salvo);
    }

    @Transactional(readOnly = true)
    public CardRespostaDTO buscarPorId(UUID id) {
        Cards card = cardsRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Card não encontrado: " + id));
        return cardMapper.toRespostaDTO(card);
    }

    @Transactional(readOnly = true)
    public ResultadoDePesquisaCardsDTO pesquisar(
            UUID idUsuario, String titulo, TIPO_ALIMENTOS alimento, int pagina, int tamanhoPagina) {

        Specification<Cards> spec = Specification.where(CardsSpec.idUsuarioEqual(idUsuario));

        if (titulo != null && !titulo.isBlank()) {
            spec = spec.and(CardsSpec.tituloLike(titulo));
        }
        if (alimento != null) {
            spec = spec.and(CardsSpec.alimentoEqual(alimento));
        }

        Page<Cards> page = cardsRepository.findAll(spec, PageRequest.of(pagina, tamanhoPagina));
        List<CardRespostaDTO> cards = page.getContent().stream()
                .map(cardMapper::toRespostaDTO).toList();

        return new ResultadoDePesquisaCardsDTO(pagina, tamanhoPagina, page.getTotalElements(), cards);
    }

    @Transactional
    public CardRespostaDTO atualizarCard(UUID id, CardDTO dto) {
        Cards card = cardsRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Card não encontrado: " + id));

        if (!card.getId_usuario().equals(dto.idUsuario())) {
            throw new OperacaoNaoPermitida("Você não pode editar cards de outro usuário.");
        }

        card.setTitulo(dto.titulo());
        card.setAlimentos(dto.alimento());
        card.setAnotacoes(dto.anotacoes());

        Cards salvo = cardsRepository.save(card);
        return cardMapper.toRespostaDTO(salvo);
    }

    @Transactional
    public void deletarCard(UUID id, UUID idUsuario) {
        Cards card = cardsRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Card não encontrado: " + id));

        if (!card.getId_usuario().equals(idUsuario)) {
            throw new OperacaoNaoPermitida("Você não pode deletar cards de outro usuário.");
        }

        cardsRepository.delete(card);
    }

    @Transactional
    public TarefaRespostaDTO criarTarefa(UUID cardId, TarefaDTO dto) {
        Cards card = cardsRepository.findById(cardId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Card não encontrado: " + cardId));

        Tarefas tarefa = new Tarefas();
        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setDataLimite(dto.dataLimite());
        tarefa.setConcluida(false);
        tarefa.setCard(card);

        card.getTarefas().add(tarefa);
        cardsRepository.save(card);

        Tarefas salva = card.getTarefas().get(card.getTarefas().size() - 1);
        return cardMapper.toTarefaRespostaDTO(salva);
    }

    @Transactional
    public TarefaRespostaDTO atualizarTarefa(UUID cardId, UUID tarefaId, TarefaDTO dto) {
        Tarefas tarefa = findTarefa(cardId, tarefaId);
        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setDataLimite(dto.dataLimite());
        tarefasRepository.save(tarefa);
        return cardMapper.toTarefaRespostaDTO(tarefa);
    }

    @Transactional
    public void deletarTarefa(UUID cardId, UUID tarefaId) {
        Tarefas tarefa = findTarefa(cardId, tarefaId);
        tarefa.getCard().getTarefas().remove(tarefa);
        tarefasRepository.delete(tarefa);
    }

    @Transactional
    public TarefaRespostaDTO concluirTarefa(UUID cardId, UUID tarefaId) {
        Tarefas tarefa = findTarefa(cardId, tarefaId);
        tarefa.setConcluida(!tarefa.getConcluida());
        tarefasRepository.save(tarefa);
        return cardMapper.toTarefaRespostaDTO(tarefa);
    }

    @Transactional(readOnly = true)
    public List<TarefaRespostaDTO> calendarioPorData(UUID userId, LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();
        return tarefasRepository.findByUsuarioAndDataLimiteBetween(userId, inicio, fim)
                .stream().map(cardMapper::toTarefaRespostaDTO).toList();
    }

    private Tarefas findTarefa(UUID cardId, UUID tarefaId) {
        Tarefas tarefa = tarefasRepository.findById(tarefaId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Tarefa não encontrada: " + tarefaId));
        if (!tarefa.getCard().getId().equals(cardId)) {
            throw new RecursoNaoEncontrado("Tarefa " + tarefaId + " não pertence ao card " + cardId);
        }
        return tarefa;
    }
}
