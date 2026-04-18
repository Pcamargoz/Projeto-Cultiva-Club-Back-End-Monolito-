package com.cultivaclub.monolito.objetos.web.mapper;

import com.cultivaclub.monolito.objetos.domain.Cards;
import com.cultivaclub.monolito.objetos.domain.Tarefas;
import com.cultivaclub.monolito.objetos.web.dto.CardDTO;
import com.cultivaclub.monolito.objetos.web.dto.CardRespostaDTO;
import com.cultivaclub.monolito.objetos.web.dto.TarefaRespostaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tarefas", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(source = "alimento", target = "alimentos")
    @Mapping(source = "idUsuario", target = "id_usuario")
    Cards toEntity(CardDTO dto);

    @Mapping(source = "alimentos", target = "alimento")
    @Mapping(source = "id_usuario", target = "idUsuario")
    CardRespostaDTO toRespostaDTO(Cards card);

    TarefaRespostaDTO toTarefaRespostaDTO(Tarefas tarefa);
}
