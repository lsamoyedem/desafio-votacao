package com.lsamoyedem.desafio_votacao.dto;

import com.lsamoyedem.desafio_votacao.enums.ResultadoVotacao;
import com.lsamoyedem.desafio_votacao.enums.StatusSessao;

public record ResultadoResponse(
        Long pautaId,
        long totalSim,
        long totalNao,
        long totalVotos,
        StatusSessao statusSessao,
        ResultadoVotacao resultadoVotacao
) {
}
