package com.lsamoyedem.desafio_votacao.dto;

import com.lsamoyedem.desafio_votacao.entity.Sessao;
import com.lsamoyedem.desafio_votacao.entity.Voto;
import com.lsamoyedem.desafio_votacao.enums.OpcaoVoto;

import java.time.LocalDateTime;

public record VotoResponse(
        Long id,
        Long sessaoId,
        String cpf,
        OpcaoVoto opcaoVoto,
        LocalDateTime createdAt
) {

    public static VotoResponse from(Voto voto) {
        return new VotoResponse(
                voto.getId(),
                voto.getSessao().getId(),
                voto.getCpf(),
                voto.getOpcaoVoto(),
                voto.getCreatedAt()
        );
    }
}
