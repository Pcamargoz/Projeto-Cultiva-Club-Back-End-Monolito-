package com.ProjetoPcamargo.projeto_exemplo.service;

import com.ProjetoPcamargo.projeto_exemplo.controller.DTO.ContratoDTO;
import com.ProjetoPcamargo.projeto_exemplo.exceptions.OperacaoNaoPermitida;
import com.ProjetoPcamargo.projeto_exemplo.exceptions.RecursoNaoEcontrado;
import com.ProjetoPcamargo.projeto_exemplo.model.ClasseUsuario;
import com.ProjetoPcamargo.projeto_exemplo.model.Contrato;
import com.ProjetoPcamargo.projeto_exemplo.model.Usuario;
import com.ProjetoPcamargo.projeto_exemplo.repository.ContratoRepository;
import com.ProjetoPcamargo.projeto_exemplo.repository.UsuarioRepository;
import com.ProjetoPcamargo.projeto_exemplo.validator.ContratoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ContratoValidator validator;

    public Contrato salvar(Contrato contrato){
        return contratoRepository.save(contrato);
    }

    public void atualizar(Contrato contrato){
        if(contrato == null){
            throw new IllegalArgumentException("E nescessario que o usuario esteja cadastrado para ser atualizado ");
        }
        if(contratoRepository.findById(contrato.getId()).isPresent()){
            // tanto atualiza como salva
            contratoRepository.save(contrato);

        }

    }
    public Optional<Contrato> obterPorId(UUID id){
        return contratoRepository.findById(id);
    }

    public List<Contrato> pesquisaPersonalizada(String fornecedor, String servico, BigDecimal preco){
        if(fornecedor != null && servico != null && preco != null){
            return contratoRepository.findByFornecedorAndServicoAndPreco(fornecedor, servico,preco);
        }
        if(fornecedor != null){
            return contratoRepository.findByFornecedor(fornecedor);
        }
        if(servico != null){
            return contratoRepository.findByServico(servico);
        }
        if(preco != null){
            contratoRepository.findByPreco(preco);
        }

        // vai retornar para achar todos que corresponderem com os ifs passados
        return contratoRepository.findAll();
    }

    public void deletar(UUID id) {

        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        contratoRepository.delete(contrato);
    }

    public List<Contrato> pesquisaByExample(String fornecedor, String servico){

        var contrato = new Contrato();
        contrato.setFornecedor(fornecedor);
        contrato.setServico(servico);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Contrato> contratoExample = Example.of(contrato, matcher);

        List<Contrato> resultado = contratoRepository.findAll(contratoExample);

        if (resultado.isEmpty()) {
            throw new RecursoNaoEcontrado(
                    "Nenhum contrato encontrado com os filtros informados");
        }

        return resultado;
    }

}
