package com.cultivaclub.monolito.objetos.web.mapper;

import com.cultivaclub.monolito.objetos.domain.Cards;
import com.cultivaclub.monolito.objetos.domain.TIPO_ALIMENTOS;
import com.cultivaclub.monolito.objetos.domain.Tarefas;
import com.cultivaclub.monolito.objetos.web.dto.CardDTO;
import com.cultivaclub.monolito.objetos.web.dto.CardRespostaDTO;
import com.cultivaclub.monolito.objetos.web.dto.TarefaRespostaDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-18T11:30:24-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CardMapperImpl implements CardMapper {

    @Override
    public Cards toEntity(CardDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Cards cards = new Cards();

        cards.setAlimentos( dto.alimento() );
        cards.setId_usuario( dto.idUsuario() );
        cards.setAnotacoes( dto.anotacoes() );
        cards.setTitulo( dto.titulo() );

        return cards;
    }

    @Override
    public CardRespostaDTO toRespostaDTO(Cards card) {
        if ( card == null ) {
            return null;
        }

        TIPO_ALIMENTOS alimento = null;
        UUID idUsuario = null;
        UUID id = null;
        String titulo = null;
        String anotacoes = null;
        List<TarefaRespostaDTO> tarefas = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        alimento = card.getAlimentos();
        idUsuario = card.getId_usuario();
        id = card.getId();
        titulo = card.getTitulo();
        anotacoes = card.getAnotacoes();
        tarefas = tarefasListToTarefaRespostaDTOList( card.getTarefas() );
        dataCadastro = card.getDataCadastro();
        dataAtualizacao = card.getDataAtualizacao();

        CardRespostaDTO cardRespostaDTO = new CardRespostaDTO( id, titulo, alimento, idUsuario, anotacoes, tarefas, dataCadastro, dataAtualizacao );

        return cardRespostaDTO;
    }

    @Override
    public TarefaRespostaDTO toTarefaRespostaDTO(Tarefas tarefa) {
        if ( tarefa == null ) {
            return null;
        }

        UUID id = null;
        String titulo = null;
        String descricao = null;
        Boolean concluida = null;
        LocalDateTime dataLimite = null;
        LocalDateTime dataCadastro = null;
        LocalDateTime dataAtualizacao = null;

        id = tarefa.getId();
        titulo = tarefa.getTitulo();
        descricao = tarefa.getDescricao();
        concluida = tarefa.getConcluida();
        dataLimite = tarefa.getDataLimite();
        dataCadastro = tarefa.getDataCadastro();
        dataAtualizacao = tarefa.getDataAtualizacao();

        TarefaRespostaDTO tarefaRespostaDTO = new TarefaRespostaDTO( id, titulo, descricao, concluida, dataLimite, dataCadastro, dataAtualizacao );

        return tarefaRespostaDTO;
    }

    protected List<TarefaRespostaDTO> tarefasListToTarefaRespostaDTOList(List<Tarefas> list) {
        if ( list == null ) {
            return null;
        }

        List<TarefaRespostaDTO> list1 = new ArrayList<TarefaRespostaDTO>( list.size() );
        for ( Tarefas tarefas : list ) {
            list1.add( toTarefaRespostaDTO( tarefas ) );
        }

        return list1;
    }
}
