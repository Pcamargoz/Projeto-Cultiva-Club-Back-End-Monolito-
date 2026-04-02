package com.ProjetoPcamargo.projeto_exemplo.validator;

import com.ProjetoPcamargo.projeto_exemplo.exceptions.OperacaoNaoPermitida;
import com.ProjetoPcamargo.projeto_exemplo.exceptions.RegistroDuplicado;
import com.ProjetoPcamargo.projeto_exemplo.model.Usuario;
import com.ProjetoPcamargo.projeto_exemplo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {

    private final UsuarioRepository repository;

    public void validar(Usuario usuario) {
        if (existeUsuarioCadastrado(usuario)) {
            throw new RegistroDuplicado("Usuário já cadastrado");
        }
    }

    private boolean existeUsuarioCadastrado(Usuario usuario) {

        Optional<Usuario> usuarioEncontrado =
                repository.findByNomeAndEmail(usuario.getNome(), usuario.getEmail());

        if (usuario.getId() == null) {
            return usuarioEncontrado.isPresent();
        }

        if (usuarioEncontrado.isPresent()) {
            return !usuario.getId().equals(usuarioEncontrado.get().getId());
        }

        return false;
    }

    public void validarSePossuiContrato(Usuario usuario) {

        if (usuario.getContratos() != null && !usuario.getContratos().isEmpty()) {
            throw new OperacaoNaoPermitida("Este usuário possui contrato ativo!");
        }
    }
}
