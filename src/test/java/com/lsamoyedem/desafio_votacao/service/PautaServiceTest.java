package com.lsamoyedem.desafio_votacao.service;

import com.lsamoyedem.desafio_votacao.dto.PautaDTO;
import com.lsamoyedem.desafio_votacao.entity.Pauta;
import com.lsamoyedem.desafio_votacao.exception.ResourceNotFoundException;
import com.lsamoyedem.desafio_votacao.repository.PautaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    void listDeveRetornarTodasAsPautas() {
        Pauta p1 = new Pauta();
        Pauta p2 = new Pauta();
        when(pautaRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Pauta> resultado = pautaService.list();

        assertThat(resultado).hasSize(2);
        verify(pautaRepository).findAll();
    }

    @Test
    void createDeveSalvarERetornarPauta() {
        PautaDTO dto = new PautaDTO("Nova Pauta", "Descrição");
        // simula o save: devolve o próprio objeto que recebeu
        when(pautaRepository.save(any(Pauta.class))).thenAnswer(inv -> inv.getArgument(0));

        Pauta resultado = pautaService.create(dto);

        assertThat(resultado.getTitle()).isEqualTo("Nova Pauta");
        assertThat(resultado.getDescription()).isEqualTo("Descrição");
        verify(pautaRepository).save(any(Pauta.class));
    }

    @Test
    void findByIdDeveRetornarPautaQuandoExiste() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        Pauta resultado = pautaService.findById(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void findByIdDeveLancarExcecaoQuandoNaoExiste() {
        //o banco não acha nada
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        // verifico que a exceção certa é lançada
        assertThatThrownBy(() -> pautaService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateDeveAtualizarPautaExistente() {
        Pauta existente = new Pauta();
        existente.setId(1L);
        existente.setTitle("Antigo");
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(pautaRepository.save(any(Pauta.class))).thenAnswer(inv -> inv.getArgument(0));

        PautaDTO dto = new PautaDTO("Novo Título", "Nova Descrição");

        Pauta resultado = pautaService.update(1L, dto);
        assertThat(resultado.getTitle()).isEqualTo("Novo Título");
    }

    @Test
    void updateDeveLancarExcecaoQuandoPautaNaoExiste() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pautaService.update(99L, new PautaDTO("X", "Y")))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(pautaRepository, never()).save(any());
    }
}