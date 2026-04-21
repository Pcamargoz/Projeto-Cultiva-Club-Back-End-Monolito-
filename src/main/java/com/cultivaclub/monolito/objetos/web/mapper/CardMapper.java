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

    /**
     * Deriva {@code concluida} a partir do {@code status} — mantém compatibilidade
     * com o frontend atual enquanto o campo {@code status} novo já fica disponível.
     *
     * Usamos {@code expression} com o nome totalmente qualificado do enum para
     * evitar que o MapStruct tente registrar conversores implícitos que
     * poderiam conflitar com este mapeamento. Não declaramos um default method
     * auxiliar aqui, pois o processor do MapStruct o interpretaria como um
     * conversor STATUS_TAREFAS → boolean e geraria código ambíguo.
     */
    @Mapping(target = "concluida",
            expression = "java(tarefa.getStatus() == com.cultivaclub.monolito.objetos.domain.STATUS_TAREFAS.CONCLUIDO)")
    TarefaRespostaDTO toTarefaRespostaDTO(Tarefas tarefa);
}
