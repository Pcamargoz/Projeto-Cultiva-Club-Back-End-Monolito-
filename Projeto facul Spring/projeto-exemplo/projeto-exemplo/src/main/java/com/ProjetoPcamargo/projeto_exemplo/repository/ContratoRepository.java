package com.ProjetoPcamargo.projeto_exemplo.repository;

import com.ProjetoPcamargo.projeto_exemplo.model.Contrato;
import com.ProjetoPcamargo.projeto_exemplo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ContratoRepository extends JpaRepository<Contrato, UUID> {



    List<Contrato> findByFornecedorAndServicoAndPreco(String fornecedor, String servico, BigDecimal preco);

    // vamos achar se o contrato ja tem um usuario
    List<Usuario> findByUsuario(Usuario usuario);

    // se o usuario ja possui um contrato relacionado
    List<Contrato> findByFornecedor(String fornecedor);

    List<Contrato> findByServico(String servico);

    List<Contrato> findByPreco(BigDecimal preco);
}
