package com.lsamoyedem.desafio_votacao.service;

import com.lsamoyedem.desafio_votacao.entity.Sessao;
import com.lsamoyedem.desafio_votacao.entity.Voto;
import com.lsamoyedem.desafio_votacao.enums.OpcaoVoto;
import com.lsamoyedem.desafio_votacao.exception.BusinessException;
import com.lsamoyedem.desafio_votacao.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private SessaoService sessaoService;

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private VotoService votoService;

    @Test
    void voteDeveRegistrarVotoQuandoSessaoAbertaECpfNovo() {
        Sessao sessao = mock(Sessao.class);
        when(sessao.getId()).thenReturn(1L);
        when(sessao.isAberta()).thenReturn(true);
        when(sessaoService.findSessaoByPautaId(1L)).thenReturn(sessao);
        when(votoRepository.existsBySessaoIdAndCpf(1L, "12345678901")).thenReturn(false);
        when(votoRepository.save(any(Voto.class))).thenAnswer(inv -> inv.getArgument(0));

        Voto resultado = votoService.vote(1L, "123.456.789-01", OpcaoVoto.SIM);

        assertThat(resultado.getCpf()).isEqualTo("12345678901"); // normalizado
        assertThat(resultado.getOpcaoVoto()).isEqualTo(OpcaoVoto.SIM);
        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    void voteDeveLancarExcecaoQuandoSessaoFechada() {
        Sessao sessao = mock(Sessao.class);
        when(sessao.isAberta()).thenReturn(false);
        when(sessaoService.findSessaoByPautaId(1L)).thenReturn(sessao);

        assertThatThrownBy(() -> votoService.vote(1L, "12345678901", OpcaoVoto.SIM))
                .isInstanceOf(BusinessException.class);
        verify(votoRepository, never()).save(any());
    }

    @Test
    void voteDeveLancarExcecaoQuandoCpfJaVotou() {
        Sessao sessao = mock(Sessao.class);
        when(sessao.getId()).thenReturn(1L);
        when(sessao.isAberta()).thenReturn(true);
        when(sessaoService.findSessaoByPautaId(1L)).thenReturn(sessao);
        when(votoRepository.existsBySessaoIdAndCpf(1L, "12345678901")).thenReturn(true);

        assertThatThrownBy(() -> votoService.vote(1L, "12345678901", OpcaoVoto.SIM))
                .isInstanceOf(BusinessException.class);
        verify(votoRepository, never()).save(any());
    }
}
