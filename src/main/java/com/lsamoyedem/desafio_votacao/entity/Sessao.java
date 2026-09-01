package com.lsamoyedem.desafio_votacao.entity;

import com.lsamoyedem.desafio_votacao.enums.StatusSessao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessoes")
@Getter
@Setter
@NoArgsConstructor
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "pauta_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Pauta pauta;
    @Column(nullable = false)
    private LocalDateTime openAt;
    @Column(nullable = false)
    private LocalDateTime closeAt;

    public Sessao(Pauta pauta, Integer minutes) {
        var now = LocalDateTime.now();
        this.pauta = pauta;
        this.openAt = now;
        this.closeAt = now.plusMinutes(minutes);
    }

    public boolean isAberta() {
        return LocalDateTime.now().isBefore(this.closeAt);
    }

    public StatusSessao getStatusSessao() {
        return isAberta() ? StatusSessao.ABERTA : StatusSessao.FINALIZADA;
    }
}
