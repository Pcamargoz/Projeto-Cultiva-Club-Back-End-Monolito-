package com.example.libraryapi.validator;

import com.example.libraryapi.exceptions.RegistroDuplicadoException;
import com.example.libraryapi.model.Autor;
import com.example.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {

    private final AutorRepository repository;

    public AutorValidator(AutorRepository repository) {
        this.repository = repository;
    }

    public void validar(Autor autor) {
        if (existeAutorCadastrado(autor)) {
            throw new RegistroDuplicadoException("Autor já cadastrado");
        }
    }

    // Verifica se já existe um autor cadastrado com os mesmos dados
    // (nome, data de nascimento e nacionalidade)
    private boolean existeAutorCadastrado(Autor autor) {

        Optional<Autor> autorEncontrado =
                repository.findByNomeAndDataNascimentoAndNacionalidade(
                        autor.getNome(),
                        autor.getDataNascimento(),
                        autor.getNacionalidade()
                );
        // POST
        if (autor.getId() == null) {
            return autorEncontrado.isPresent();

        }
        // PUT
        return !autor.getId().equals(autorEncontrado.get().getId()) && autorEncontrado.isPresent();
    }
}
