package com.ProjetoPcamargo.projeto_exemplo.service;

import com.ProjetoPcamargo.projeto_exemplo.model.Contrato;
import com.ProjetoPcamargo.projeto_exemplo.model.Usuario;
import com.ProjetoPcamargo.projeto_exemplo.repository.ContratoRepository;
import com.ProjetoPcamargo.projeto_exemplo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssociacaoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void associar(Usuario usuario, Contrato contrato) {

        List<Contrato> contratos = usuario.getContratos();

        // vendo se o usuario ja nao tem contrato associado
        if (contratos == null) {
            contratos = new ArrayList<>();
        }

        contratos.add(contrato);

        // fazendo as associações
        contrato.setUsuario(usuario);
        usuario.setContratos(contratos);
    }

}
