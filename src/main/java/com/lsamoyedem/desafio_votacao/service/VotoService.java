package com.lsamoyedem.desafio_votacao.service;

import com.lsamoyedem.desafio_votacao.dto.ResultadoResponse;
import com.lsamoyedem.desafio_votacao.entity.Sessao;
import com.lsamoyedem.desafio_votacao.entity.Voto;
import com.lsamoyedem.desafio_votacao.enums.OpcaoVoto;
import com.lsamoyedem.desafio_votacao.enums.ResultadoVotacao;
import com.lsamoyedem.desafio_votacao.exception.BusinessException;
import com.lsamoyedem.desafio_votacao.repository.VotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    private final SessaoService sessaoService;
    private final VotoRepository votoRepository;

    public VotoService(SessaoService sessaoService, VotoRepository votoRepository) {
        this.sessaoService = sessaoService;
        this.votoRepository = votoRepository;
    }

    public boolean hasVoted(Long sessaoId, String cpf) {
        return votoRepository.existsBySessaoIdAndCpf(sessaoId, cpf);
    }

    @Transactional
    public Voto vote(Long pautaId, String cpf, OpcaoVoto opcaoVoto) {
        cpf = cpf.replaceAll("\\D", "");
        Sessao sessao = sessaoService.findSessaoByPautaId(pautaId);
        if (!sessao.isAberta()) {
            throw new BusinessException("A sessão de votação da pauta: " + pautaId + " já foi encerrada");
        }
        boolean userVoted = hasVoted(sessao.getId(), cpf);
        if (userVoted) {
            throw new BusinessException("O usuário com CPF " + cpf + " já votou nesta sessão");
        }
        Voto voto = new Voto();
        voto.setCpf(cpf);
        voto.setOpcaoVoto(opcaoVoto);
        voto.setSessao(sessao);
        votoRepository.save(voto);
        return voto;
    }

    public ResultadoResponse getResultado(Long pautaId) {
        Sessao sessao = sessaoService.findSessaoByPautaId(pautaId);
        long sim = votoRepository.countBySessaoIdAndOpcaoVoto(sessao.getId(), OpcaoVoto.SIM);
        long nao = votoRepository.countBySessaoIdAndOpcaoVoto(sessao.getId(), OpcaoVoto.NAO);
        ResultadoVotacao resultadoVotacao = ResultadoVotacao.EMPATE;
        if (sim > nao) {
            resultadoVotacao = ResultadoVotacao.APROVADA;
        } else if (nao > sim) {
            resultadoVotacao = ResultadoVotacao.REPROVADA;
        }
        return new ResultadoResponse(pautaId, sim, nao, sim + nao, sessao.getStatusSessao(), resultadoVotacao);
    }
}
