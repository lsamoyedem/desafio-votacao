package com.lsamoyedem.desafio_votacao.service;

import com.lsamoyedem.desafio_votacao.dto.SessaoResponse;
import com.lsamoyedem.desafio_votacao.entity.Pauta;
import com.lsamoyedem.desafio_votacao.entity.Sessao;
import com.lsamoyedem.desafio_votacao.exception.BusinessException;
import com.lsamoyedem.desafio_votacao.exception.ResourceNotFoundException;
import com.lsamoyedem.desafio_votacao.repository.PautaRepository;
import com.lsamoyedem.desafio_votacao.repository.SessaoRepository;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.ContextNotEmptyException;
import java.util.Optional;

@Service
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;

    public SessaoService (SessaoRepository sessaoRepository, PautaService pautaService) {
        this.sessaoRepository = sessaoRepository;
        this.pautaService = pautaService;
    }

    public Sessao getSessaoByPautaId(Long pautaId) {
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
