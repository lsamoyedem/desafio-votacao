package com.lsamoyedem.desafio_votacao.dto;

import com.lsamoyedem.desafio_votacao.enums.OpcaoVoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VotoDTO(
        @NotNull OpcaoVoto opcaoVoto,
        @NotBlank String cpf
) {
}
