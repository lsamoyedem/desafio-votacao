package com.lsamoyedem.desafio_votacao.repository;

import com.lsamoyedem.desafio_votacao.entity.Voto;
import com.lsamoyedem.desafio_votacao.enums.OpcaoVoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsBySessaoIdAndCpf(Long sessaoId, String cpf);

    Long countBySessaoIdAndOpcaoVoto(Long sessaoId, OpcaoVoto opcaoVoto);

}
