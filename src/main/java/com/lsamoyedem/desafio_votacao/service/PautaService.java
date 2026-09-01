package com.lsamoyedem.desafio_votacao.service;

import com.lsamoyedem.desafio_votacao.dto.PautaDTO;
import com.lsamoyedem.desafio_votacao.entity.Pauta;
import com.lsamoyedem.desafio_votacao.exception.ResourceNotFoundException;
import com.lsamoyedem.desafio_votacao.repository.PautaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PautaService {

    private final PautaRepository pautaRepository;

    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public Pauta findById(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta " + id + " não encontrada"));
    }

    public List<Pauta> list() {
        return pautaRepository.findAll();
    }

    public Pauta create(PautaDTO pautaDTO) {
        Pauta pauta = new Pauta();
        pauta.setTitle(pautaDTO.title());
        pauta.setDescription(pautaDTO.description());
        return pautaRepository.save(pauta);
    }

    public Pauta update(Long id, PautaDTO pautaDTO) {
        Pauta pauta = findById(id);
        pauta.setTitle(pautaDTO.title());
        pauta.setDescription(pautaDTO.description());
        return pautaRepository.save(pauta);
    }

    public void delete(Long id) {
        Pauta pauta = findById(id);
        pautaRepository.delete(pauta);
    }
}
