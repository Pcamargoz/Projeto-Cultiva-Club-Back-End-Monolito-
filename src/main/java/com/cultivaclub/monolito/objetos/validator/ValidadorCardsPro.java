package com.cultivaclub.monolito.objetos.validator;

import com.cultivaclub.monolito.common.exception.OperacaoNaoPermitida;
import com.cultivaclub.monolito.objetos.repository.CardsRepository;
import com.cultivaclub.monolito.usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Valida regras de negócio do plano do usuário ao manipular cards.
 *
 * Na versão de microsserviços este validador dependia de um cache local
 * populado via RabbitMQ (UsuarioRoleCache). No monólito consultamos o
 * UsuarioService diretamente, eliminando a necessidade de mensageria e
 * de uma tabela de cache.
 */
@Component
@RequiredArgsConstructor
public class ValidadorCardsPro {

    private static final int LIMITE_CARDS_USUARIO = 5;

    private final UsuarioService usuarioService;
    private final CardsRepository cardsRepository;

    public boolean isUsuarioPro(UUID userId) {
        return usuarioService.isUsuarioPro(userId);
    }

    public void validarLimiteCards(UUID userId) {
        if (isUsuarioPro(userId)) {
            return;
        }
        long totalCards = cardsRepository.countByIdUsuario(userId);
        if (totalCards >= LIMITE_CARDS_USUARIO) {
            throw new OperacaoNaoPermitida(
                    "Limite de " + LIMITE_CARDS_USUARIO + " cards atingido. Faça upgrade para UsuarioPro para cards ilimitados."
            );
        }
    }
}
