package br.com.pedrohenrick.agendatech.model;

import br.com.pedrohenrick.agendatech.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_AGENDAMENTOS")
public class Agendamento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Data e Hora do agendamento (Ex: 2025-12-25T14:30:00)
    @Column(nullable = false)
    private LocalDateTime dataHora;

    // Campo opcional para o cliente escrever algo (Ex: "Tenho alergia a tal produto")
    @Column(length = 500)
    private String observacoes;

    // Status atual (Começa como PENDENTE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    // ================= RELACIONAMENTOS =================

    // 1. Quem é o cliente?
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    // 2. Qual serviço será prestado?
    // (Lembrando que o Serviço já tem o Profissional vinculado a ele)
    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;
}