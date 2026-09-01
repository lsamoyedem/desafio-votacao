package com.lsamoyedem.desafio_votacao.entity;

import com.lsamoyedem.desafio_votacao.enums.OpcaoVoto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "votos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sessao_id", "cpf"})
})
@Getter @Setter
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;
    @Column(nullable = false)
    private String cpf;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OpcaoVoto opcaoVoto;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
