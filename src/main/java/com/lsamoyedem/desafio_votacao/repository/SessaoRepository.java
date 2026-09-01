package com.lsamoyedem.desafio_votacao.repository;

import com.lsamoyedem.desafio_votacao.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    boolean existsByPautaId(Long pautaId);

    Optional<Sessao> findByPautaId(Long pautaId);
}
