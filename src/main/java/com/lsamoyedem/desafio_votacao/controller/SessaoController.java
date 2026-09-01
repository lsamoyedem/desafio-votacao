package com.lsamoyedem.desafio_votacao.controller;

import com.lsamoyedem.desafio_votacao.dto.SessaoResponse;
import com.lsamoyedem.desafio_votacao.entity.Sessao;
import com.lsamoyedem.desafio_votacao.service.SessaoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/pautas/{pautaId}/sessoes")
public class SessaoController {

    private final SessaoService sessaoService;

    public SessaoController(SessaoService sessaoService) {
        this.sessaoService = sessaoService;
    }

    @PostMapping
    public SessaoResponse openSessao(@PathVariable Long pautaId,
                                     @RequestParam(required = false) Integer minutes) {
        Sessao sessao = sessaoService.open(pautaId, minutes);
        return SessaoResponse.from(sessao);
    }

    @GetMapping
    public SessaoResponse findSessao(@PathVariable Long pautaId) {
        Sessao sessao = sessaoService.findSessaoByPautaId(pautaId);
        return SessaoResponse.from(sessao);
    }
}
