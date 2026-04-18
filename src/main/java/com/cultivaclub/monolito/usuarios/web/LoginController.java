package com.cultivaclub.monolito.usuarios.web;

import com.cultivaclub.monolito.usuarios.domain.Usuario;
import com.cultivaclub.monolito.usuarios.service.UsuarioService;
import com.cultivaclub.monolito.usuarios.web.dto.LoginDTO;
import com.cultivaclub.monolito.usuarios.web.dto.LoginRespostaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<LoginRespostaDTO> login(@RequestBody @Valid LoginDTO dto) {
        Usuario usuario = service.obterPorLoginAndSenha(dto.login(), dto.senha());

        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        // Token simples baseado em UUID (substituir por JWT no futuro)
        String token = UUID.randomUUID().toString();

        LoginRespostaDTO resposta = new LoginRespostaDTO(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getEmail(),
                usuario.getNome(),
                usuario.getRole(),
                token
        );

        return ResponseEntity.ok(resposta);
    }
}
