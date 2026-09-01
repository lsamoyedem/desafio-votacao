package com.lsamoyedem.desafio_votacao.service;

import com.lsamoyedem.desafio_votacao.entity.Pauta;
import com.lsamoyedem.desafio_votacao.entity.Sessao;
import com.lsamoyedem.desafio_votacao.exception.BusinessException;
import com.lsamoyedem.desafio_votacao.exception.ResourceNotFoundException;
import com.lsamoyedem.desafio_votacao.repository.SessaoRepository;
import org.springframework.stereotype.Service;

@Service
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;

    public SessaoService(SessaoRepository sessaoRepository, PautaService pautaService) {
        this.sessaoRepository = sessaoRepository;
        this.pautaService = pautaService;
    }

    public Sessao findSessaoByPautaId(Long pautaId) {
        return sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new ResourceNotFoundException("Sessão ainda não aberta para a pauta: " + pautaId));
    }

    public Sessao open(Long pautaId, Integer minutes) {
        Pauta pauta = pautaService.findById(pautaId);
        boolean existsSessao = sessaoRepository.existsByPautaId(pautaId);
        if (existsSessao) {
            throw new BusinessException("Sessão já criada para a pauta " + pautaId);
        }
        minutes = minutes == null ? 1 : minutes;
        Sessao sessao = new Sessao(pauta, minutes);
        return sessaoRepository.save(sessao);
    }
}
