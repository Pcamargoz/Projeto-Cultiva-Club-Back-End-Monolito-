package com.cultivaclub.monolito.objetos.web.dto;

import java.util.List;

public record ResultadoDePesquisaCardsDTO(
        int pagina,
        int tamanhoPagina,
        long totalResultados,
        List<CardRespostaDTO> cards
) {}
