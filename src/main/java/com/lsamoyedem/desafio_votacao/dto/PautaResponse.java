package com.lsamoyedem.desafio_votacao.dto;

import com.lsamoyedem.desafio_votacao.entity.Pauta;

public record PautaResponse(
        Long id,
        String title,
        String description
) {

    public static PautaResponse from(Pauta pauta) {
        return new PautaResponse(
                pauta.getId(),
                pauta.getTitle(),
                pauta.getDescription()
        );
    }
}
