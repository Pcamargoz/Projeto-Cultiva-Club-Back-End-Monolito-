package com.example.libraryapi.controller.dto;

import com.example.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

// queremos criar uma entidade a parte
// um objeto a parte que vamos usar apenas para representar o json
public record AutorDTO(
        UUID id,
        // Para String não vim nula
        @NotBlank(message = "Campo Obrigatorio")
        @Size(max = 100, message = "Campo Fora do Tamanho Padrão")
        String nome,
        // para campos
        @NotNull(message = "Campo Obrigatorio")
        //@Future so pode ser uma data futura
        @Past(message = "Não pode ser uma data futura")
        LocalDate dataNascimento,
        @NotBlank(message = "Campo Obrigatorio")
        @Size(max = 50, min = 2, message = "Campo Fora do Tamanho Padrão")
        String nacionalidade) {

            public Autor mapearParaAutor(){
                Autor autor = new Autor();
                // colocando os nomes que vamos receber do DTO
                // por que a unica coisa que a gente não vai selecionar e o id
                // e o dto e uma classe a parte para que a gente sete a penas o que
                // precisa ser inserido
                autor.setNome(this.nome);
                autor.setDataNascimento(this.dataNascimento);
                autor.setNacionalidade(this.nacionalidade);
                return autor;
            }
}
