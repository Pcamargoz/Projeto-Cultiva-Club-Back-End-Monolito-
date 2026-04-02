package com.ProjetoPcamargo.projeto_exemplo.validator;

import com.ProjetoPcamargo.projeto_exemplo.exceptions.RegistroDuplicado;
import com.ProjetoPcamargo.projeto_exemplo.model.Contrato;
import com.ProjetoPcamargo.projeto_exemplo.model.Usuario;
import com.ProjetoPcamargo.projeto_exemplo.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContratoValidator {

    private final ContratoRepository contratoRepository;
    // pensar em algo para implementar como validação de contratos
}
