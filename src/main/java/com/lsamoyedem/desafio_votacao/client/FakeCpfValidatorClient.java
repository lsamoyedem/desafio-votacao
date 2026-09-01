package com.lsamoyedem.desafio_votacao.client;

import com.lsamoyedem.desafio_votacao.enums.StatusCpf;
import com.lsamoyedem.desafio_votacao.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class FakeCpfValidatorClient implements CpfValidatorClient {

    private final Random random = new Random();

    @Override
    public StatusCpf validar(String cpf) {
        // 20% de chance de o CPF "não existir"
        if (random.nextInt(100) < 20) {
            throw new ResourceNotFoundException("CPF não encontrado: " + cpf);
        }
        // sorteia se pode ou não votar
        return random.nextBoolean()
                ? StatusCpf.ABLE_TO_VOTE
                : StatusCpf.UNABLE_TO_VOTE;
    }
}
