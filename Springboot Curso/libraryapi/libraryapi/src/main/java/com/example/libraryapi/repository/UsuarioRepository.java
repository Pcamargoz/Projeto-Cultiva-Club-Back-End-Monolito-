package com.example.libraryapi.repository;

import com.example.libraryapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    // podemos retornar o optional ou o usuario ja direto
    Usuario findByLogin(String login);

    Usuario findByEmail(String email);
}
