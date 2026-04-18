package com.cultivaclub.monolito.usuarios.service;

import com.cultivaclub.monolito.common.exception.RecursoNaoEncontrado;
import com.cultivaclub.monolito.usuarios.domain.ROLES;
import com.cultivaclub.monolito.usuarios.domain.Usuario;
import com.cultivaclub.monolito.usuarios.repository.UsuarioRepository;
import com.cultivaclub.monolito.usuarios.validator.ValidatorUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ValidatorUsuario validator;
    private final PasswordEncoder encoder;

    public Usuario salvar(Usuario usuario) {
        validator.validarParaCriar(usuario);
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        return repository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Optional<Usuario> obterPorId(UUID id) {
        return repository.findById(id);
    }

    public Usuario atualizar(Usuario usuario) {
        validator.validarParaAtualizar(usuario);
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        return repository.save(usuario);
    }

    public Usuario atualizarSemReencodarSenha(Usuario usuario) {
        validator.validarParaAtualizar(usuario);
        return repository.save(usuario);
    }

    public Usuario atualizarRole(UUID id, ROLES novaRole) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Usuário não encontrado."));
        usuario.setRole(novaRole);
        return repository.save(usuario);
    }

    public boolean senhaConfere(String senhaRaw, String senhaEncoded) {
        return encoder.matches(senhaRaw, senhaEncoded);
    }

    public void deletar(UUID id) {
        repository.deleteById(id);
    }

    public Usuario obterPorLoginAndSenha(String login, String senha) {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null) return null;
        if (!encoder.matches(senha, usuario.getSenha())) return null;
        return usuario;
    }

    public Usuario obterPorLogin(String login) {
        return repository.findByLogin(login);
    }

    public Usuario obterPorEmail(String email) {
        return repository.findByEmail(email);
    }

    /**
     * Utilitário interno do monólito para consulta de role por outro módulo
     * (substitui a mensageria RabbitMQ + cache usada na versão de microsserviços).
     */
    public boolean isUsuarioPro(UUID id) {
        return repository.findById(id)
                .map(u -> u.getRole() == ROLES.UsuarioPro || u.getRole() == ROLES.ADMIN)
                .orElse(false);
    }
}
