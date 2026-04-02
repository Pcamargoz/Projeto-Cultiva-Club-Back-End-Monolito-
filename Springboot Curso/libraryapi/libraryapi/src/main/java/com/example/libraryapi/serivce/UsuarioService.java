package com.example.libraryapi.serivce;

import com.example.libraryapi.model.Usuario;
import com.example.libraryapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    // precisamos criptogrfar o usuario para salvarmos a senha no banco
    private final PasswordEncoder encoder;

    // salvando so pela senha por teste
    public void salvar(Usuario usuario){
        var senha = usuario.getSenha();
        usuario.setSenha(encoder.encode(senha));
        repository.save(usuario);
    }
    public Usuario obterPorLogin(String login){
        return repository.findByLogin(login);
    }
    public Usuario obterPorEmail(String email){
        return repository.findByEmail(email);
    }
}
