package com.cultivaclub.monolito.usuarios.validator;

import com.cultivaclub.monolito.common.exception.RegistroDuplicado;
import com.cultivaclub.monolito.usuarios.domain.Usuario;
import com.cultivaclub.monolito.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidatorUsuario {

    private final UsuarioRepository repository;

    public void validarParaCriar(Usuario usuario) {
        validarDuplicidade(usuario.getLogin(), usuario.getEmail(), null);
    }

    public void validarParaAtualizar(Usuario usuario) {
        validarDuplicidade(usuario.getLogin(), usuario.getEmail(), usuario.getId());
    }

    private void validarDuplicidade(String login, String email, UUID idAtual) {
        Usuario usuarioLogin = repository.findByLogin(login);
        if (usuarioLogin != null && (idAtual == null || !usuarioLogin.getId().equals(idAtual))) {
            throw new RegistroDuplicado("Já existe usuário com esse login.");
        }

        Usuario usuarioEmail = repository.findByEmail(email);
        if (usuarioEmail != null && (idAtual == null || !usuarioEmail.getId().equals(idAtual))) {
            throw new RegistroDuplicado("Já existe usuário com esse email.");
        }
    }
}
