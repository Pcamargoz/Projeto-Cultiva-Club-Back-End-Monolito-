package com.ProjetoPcamargo.projeto_exemplo.repository;

import com.ProjetoPcamargo.projeto_exemplo.model.ClasseUsuario;
import com.ProjetoPcamargo.projeto_exemplo.model.Contrato;
import com.ProjetoPcamargo.projeto_exemplo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // fazer as operações de buscar como findByusuario e etc
    List<Usuario> findByNome(String nome);
    List<Usuario> findByEmail(String email);
    List<Usuario> findByTipoCliente(ClasseUsuario tipoCliente);

    List<Usuario> findByNomeAndTipoClienteAndEmail(
            String name,
            ClasseUsuario tipoCliente,
            String email
    );

    Optional<Usuario> findByNomeAndEmail(String nome, String email);

    // vendo se esxiste um contrato associado com jpql
    @Query("""
    SELECT COUNT(u) > 0
    FROM Usuario u
    WHERE :contrato MEMBER OF u.contratos
    """)
    boolean existsByContrato(@Param("contrato") Contrato contrato);
}
