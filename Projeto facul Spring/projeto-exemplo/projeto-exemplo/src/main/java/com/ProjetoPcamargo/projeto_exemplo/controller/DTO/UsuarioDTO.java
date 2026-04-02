package com.ProjetoPcamargo.projeto_exemplo.controller.DTO;

import com.ProjetoPcamargo.projeto_exemplo.model.ClasseUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

// implementar essa classe para usario dto para que a gente possa cadastrar usuario sem alguns campos nescessarios.
public record UsuarioDTO(UUID id,
                         @NotBlank(message = " Campo Obrigatorio ! ")
                         String nome,
                         @NotBlank(message = "Campo Obrigatorio !")
                         String email,
                         @NotBlank(message = " Campo Obrigatorio ! ")
                         ClasseUsuario tipoCliente,
                         BigDecimal moedas){


}
