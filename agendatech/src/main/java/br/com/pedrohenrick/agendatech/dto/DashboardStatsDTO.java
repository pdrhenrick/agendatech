package br.com.pedrohenrick.agendatech.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardStatsDTO {
    private BigDecimal faturamentoTotal;
    private Long agendamentosHoje;
    private Long totalClientes;

    public DashboardStatsDTO(BigDecimal faturamentoTotal, Long agendamentosHoje, Long totalClientes) {
        this.faturamentoTotal = faturamentoTotal != null ? faturamentoTotal : BigDecimal.ZERO;
        this.agendamentosHoje = agendamentosHoje;
        this.totalClientes = totalClientes;
    }
}