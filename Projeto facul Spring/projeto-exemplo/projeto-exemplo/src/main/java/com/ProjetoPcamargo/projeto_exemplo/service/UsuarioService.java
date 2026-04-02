package com.ProjetoPcamargo.projeto_exemplo.service;

import com.ProjetoPcamargo.projeto_exemplo.exceptions.OperacaoNaoPermitida;
import com.ProjetoPcamargo.projeto_exemplo.exceptions.RecursoNaoEcontrado;
import com.ProjetoPcamargo.projeto_exemplo.model.ClasseUsuario;
import com.ProjetoPcamargo.projeto_exemplo.model.Usuario;
import com.ProjetoPcamargo.projeto_exemplo.repository.ContratoRepository;
import com.ProjetoPcamargo.projeto_exemplo.repository.UsuarioRepository;
import com.ProjetoPcamargo.projeto_exemplo.validator.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final ContratoRepository contratoRepository;
    private final UsuarioRepository usuariorepository;
    private final UsuarioValidator validator;

    public Usuario salvar(Usuario usario) {
        validator.validar(usario);
        return usuariorepository.save(usario);
    }

    public void atualizar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("E nescessario que o usuario esteja cadastrado para ser atualizado ");
        }
        validator.validar(usuario);
        // tanto atualiza como salva
        usuariorepository.save(usuario);

    }

    // vai me retornar apenas o usuario que encontrou
    public Optional<Usuario> obterPorId(UUID id){
        return usuariorepository.findById(id);
    }

    public void deletar(UUID id) {
        Usuario usuario = usuariorepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // ele pega a lista e verifica se tem elementos dentro
        if (usuario.getContratos() != null && !usuario.getContratos().isEmpty()) {
            throw new OperacaoNaoPermitida("Usuário já possui contratos vinculados!");
        }

        usuariorepository.delete(usuario);
    }

    public List<Usuario> pesquisaByExample(String nome, ClasseUsuario tipoDoCliente){

        var usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setTipoCliente(tipoDoCliente);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Usuario> usuarioExample = Example.of(usuario, matcher);

        List<Usuario> resultado = usuariorepository.findAll(usuarioExample);

        if (resultado.isEmpty()) {
            throw new RecursoNaoEcontrado("Nenhum usuário encontrado com os filtros informados");
        }

        return resultado;
    }


    public List<Usuario> pesquisaPersonalizada(String nome, ClasseUsuario tipoCliente, String email) {
        if (nome != null && tipoCliente != null && email != null) {
            return usuariorepository.findByNomeAndTipoClienteAndEmail(nome, tipoCliente, email);
        }
        if (nome != null) {
            return usuariorepository.findByNome(nome);
        }
        if (tipoCliente != null) {
            return usuariorepository.findByTipoCliente(tipoCliente);
        }
        if (email != null) {
            usuariorepository.findByEmail(email);
        }
        // vai retornar para achar todos que corresponderem com os ifs passados
        return usuariorepository.findAll();
    }


}
