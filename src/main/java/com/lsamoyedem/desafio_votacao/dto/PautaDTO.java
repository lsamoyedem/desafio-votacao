package com.lsamoyedem.desafio_votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PautaDTO (@NotBlank @Size(max = 120) String title,
                        @Size(max = 120) String description) {
}
