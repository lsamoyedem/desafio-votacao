package com.lsamoyedem.desafio_votacao.client;

import com.lsamoyedem.desafio_votacao.enums.StatusCpf;

public interface CpfValidatorClient {
    StatusCpf validar(String cpf);
}
