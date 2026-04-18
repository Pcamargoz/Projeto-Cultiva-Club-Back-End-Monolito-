package com.cultivaclub.monolito.usuarios.repository;

import com.cultivaclub.monolito.usuarios.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Usuario findByLogin(String login);
    Usuario findByEmail(String email);
}
