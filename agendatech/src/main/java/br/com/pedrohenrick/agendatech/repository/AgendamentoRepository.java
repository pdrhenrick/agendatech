package br.com.pedrohenrick.agendatech.repository;

import br.com.pedrohenrick.agendatech.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {
    
    // Buscar histórico de um cliente
    List<Agendamento> findByClienteId(UUID clienteId);

    // Buscar agenda de um profissional
    List<Agendamento> findByServicoProfissionalId(UUID profissionalId);

    // --- QUERIES PARA O DASHBOARD ---

    // 1. Faturação: Soma o preço dos serviços CONCLUÍDOS deste profissional
    @Query("SELECT SUM(a.servico.preco) FROM Agendamento a WHERE a.servico.profissional.id = :profissionalId AND a.status = 'CONCLUIDO'")
    BigDecimal calcularFaturamentoTotal(@Param("profissionalId") UUID profissionalId);

    // 2. Clientes: Conta quantos clientes ÚNICOS este profissional já atendeu
    @Query("SELECT COUNT(DISTINCT a.cliente) FROM Agendamento a WHERE a.servico.profissional.id = :profissionalId")
    Long contarClientesUnicos(@Param("profissionalId") UUID profissionalId);

    // 3. Hoje: Conta quantos agendamentos existem num intervalo de tempo (Ex: Hoje 00:00 até Hoje 23:59)
    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.servico.profissional.id = :profissionalId AND a.dataHora BETWEEN :inicio AND :fim")
    Long contarAgendamentosNoPeriodo(@Param("profissionalId") UUID profissionalId, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}