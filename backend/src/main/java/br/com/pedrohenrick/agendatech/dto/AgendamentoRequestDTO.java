package br.com.pedrohenrick.agendatech.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AgendamentoRequestDTO {
    
    // Qual serviço o cliente quer?
    private UUID servicoId;

    // Quando? (Formato: 2025-12-25T15:30:00)
    private LocalDateTime dataHora;

    // Alguma observação?
    private String observacoes;
}