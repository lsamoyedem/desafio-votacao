package com.lsamoyedem.desafio_votacao.dto;

import com.lsamoyedem.desafio_votacao.entity.Sessao;

import java.time.LocalDateTime;

public record SessaoResponse(
        Long id,
        Long pautaId,
        LocalDateTime openAt,
        LocalDateTime closeAt,
        boolean aberta
) {
    public static SessaoResponse from(Sessao sessao) {
        return new SessaoResponse(
                sessao.getId(),
                sessao.getPauta().getId(),
                sessao.getOpenAt(),
                sessao.getCloseAt(),
                sessao.isAberta()
        );
    }
}
