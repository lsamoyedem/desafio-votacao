package com.lsamoyedem.desafio_votacao.controller;

import com.lsamoyedem.desafio_votacao.dto.PautaDTO;
import com.lsamoyedem.desafio_votacao.dto.PautaResponse;
import com.lsamoyedem.desafio_votacao.entity.Pauta;
import com.lsamoyedem.desafio_votacao.service.PautaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pautas")
public class PautaController {

    private final PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @GetMapping
    public List<Pauta> getPauta() {
        return pautaService.list();
    }

    @PostMapping
    public PautaResponse create(@Valid @RequestBody PautaDTO pautaDTO) {
        Pauta pauta = pautaService.create(pautaDTO);
        return PautaResponse.from(pauta);
    }

    @PutMapping("/{id}")
    public PautaResponse update(@PathVariable Long id, @Valid @RequestBody PautaDTO pautaDTO) {
        Pauta pauta = pautaService.update(id, pautaDTO);
        return PautaResponse.from(pauta);
    }
}
