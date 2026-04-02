package com.ProjetoPcamargo.projeto_exemplo.controller.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ContratoDTO(
        UUID id,
                          @NotBlank(message = "Campo Obrigatorio")
                           String fornecedor,
                          @NotBlank(message = "Campo Obrigatorio")
                           String servico,
                          @NotNull(message = "Campo Obrigatorio")
                           BigDecimal preco,
                          UUID idUsuario){



}
