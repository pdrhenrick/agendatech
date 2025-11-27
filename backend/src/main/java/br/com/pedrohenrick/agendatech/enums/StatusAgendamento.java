package br.com.pedrohenrick.agendatech.enums;

public enum StatusAgendamento {
    PENDENTE,   // Cliente solicitou, profissional ainda não viu
    CONFIRMADO, // Profissional aceitou
    CANCELADO,  // Alguém desistiu
    CONCLUIDO   // Serviço já foi realizado
}