package com.cultivaclub.monolito.common.config;

import com.cultivaclub.monolito.usuarios.domain.ROLES;
import com.cultivaclub.monolito.usuarios.domain.Usuario;
import com.cultivaclub.monolito.usuarios.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.cors.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("name");

        if (email == null || email.isBlank()) {
            response.sendRedirect(frontendUrl + "/login?error=oauth_no_email");
            return;
        }

        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setLogin(gerarLoginUnico(email));
            usuario.setNome(nome != null ? nome : email);
            usuario.setSenha(passwordEncoder.encode(UUID.randomUUID().toString()));
            usuario.setRole(ROLES.Usuario);
            usuario = usuarioRepository.save(usuario);
        }

        String token = UUID.randomUUID().toString();

        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/callback")
                .queryParam("token", token)
                .queryParam("id", usuario.getId())
                .queryParam("login", usuario.getLogin())
                .queryParam("email", usuario.getEmail())
                .queryParam("nome", usuario.getNome() == null ? "" : usuario.getNome())
                .queryParam("role", usuario.getRole().name())
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String gerarLoginUnico(String email) {
        String base = email.split("@")[0].replaceAll("[^a-zA-Z0-9._-]", "");
        if (base.length() > 40) base = base.substring(0, 40);
        String candidato = base;
        int sufixo = 1;
        while (usuarioRepository.findByLogin(candidato) != null) {
            candidato = base + sufixo++;
            if (candidato.length() > 50) candidato = candidato.substring(0, 50);
        }
        return candidato;
    }
}
