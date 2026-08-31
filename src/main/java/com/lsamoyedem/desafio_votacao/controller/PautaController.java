package com.lsamoyedem.desafio_votacao.controller;

import com.lsamoyedem.desafio_votacao.dto.PautaDTO;
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
    public List<Pauta> getPauta(){
        return pautaService.list();
    }

    @PostMapping
    public Pauta create(@Valid @RequestBody PautaDTO pautaDTO){
        return pautaService.create(pautaDTO);
    }

    @PutMapping("/{id}")
    public Pauta update(@PathVariable Long id,  @Valid @RequestBody PautaDTO pautaDTO){
        return pautaService.update(id, pautaDTO);
    }
}
