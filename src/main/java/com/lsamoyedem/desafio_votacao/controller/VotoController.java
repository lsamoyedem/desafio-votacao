package com.lsamoyedem.desafio_votacao.controller;

import com.lsamoyedem.desafio_votacao.dto.ResultadoResponse;
import com.lsamoyedem.desafio_votacao.dto.VotoDTO;
import com.lsamoyedem.desafio_votacao.dto.VotoResponse;
import com.lsamoyedem.desafio_votacao.entity.Voto;
import com.lsamoyedem.desafio_votacao.service.VotoService;
import jakarta.validation.Valid;
import org.hibernate.sql.results.jdbc.internal.ResultSetAccess;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/pautas/{pautaId}/votos")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VotoResponse vote(@PathVariable Long pautaId, @Valid @RequestBody VotoDTO votoDTO) {
        Voto voto = votoService.vote(pautaId, votoDTO.cpf(), votoDTO.opcaoVoto());
        return VotoResponse.from(voto);
    }

    @GetMapping("/resultado")
    public ResultadoResponse resultado(@PathVariable Long pautaId) {
        return votoService.getResultado(pautaId);
    }
}
