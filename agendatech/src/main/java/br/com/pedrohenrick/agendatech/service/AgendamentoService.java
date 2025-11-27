package br.com.pedrohenrick.agendatech.service;

import br.com.pedrohenrick.agendatech.dto.AgendamentoRequestDTO;
import br.com.pedrohenrick.agendatech.dto.DashboardStatsDTO; // <--- NOVO IMPORT
import br.com.pedrohenrick.agendatech.enums.Role;
import br.com.pedrohenrick.agendatech.enums.StatusAgendamento;
import br.com.pedrohenrick.agendatech.model.Agendamento;
import br.com.pedrohenrick.agendatech.model.Servico;
import br.com.pedrohenrick.agendatech.model.Usuario;
import br.com.pedrohenrick.agendatech.repository.AgendamentoRepository;
import br.com.pedrohenrick.agendatech.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal; // <--- NOVO IMPORT
import java.time.LocalDate;     // <--- NOVO IMPORT
import java.time.LocalDateTime; // <--- NOVO IMPORT
import java.time.LocalTime;     // <--- NOVO IMPORT
import java.util.List;
import java.util.UUID;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    public Agendamento criarAgendamento(AgendamentoRequestDTO dados, Usuario cliente) {
        Servico servico = servicoRepository.findById(dados.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        Usuario profissional = servico.getProfissional();

        List<Agendamento> agendaDoProfissional = agendamentoRepository.findByServicoProfissionalId(profissional.getId());

        for (Agendamento agendamentoExistente : agendaDoProfissional) {
            if (agendamentoExistente.getDataHora().isEqual(dados.getDataHora()) 
                && agendamentoExistente.getStatus() != StatusAgendamento.CANCELADO) {
                
                throw new RuntimeException("Horário indisponível! O profissional já tem um atendimento agendado para " + dados.getDataHora());
            }
        }

        Agendamento novoAgendamento = new Agendamento();
        novoAgendamento.setCliente(cliente);
        novoAgendamento.setServico(servico);
        novoAgendamento.setDataHora(dados.getDataHora());
        novoAgendamento.setObservacoes(dados.getObservacoes());
        novoAgendamento.setStatus(StatusAgendamento.PENDENTE);

        return agendamentoRepository.save(novoAgendamento);
    }

    public List<Agendamento> listarPorUsuario(Usuario usuario) {
        if (usuario.getRole() == Role.PROFISSIONAL) {
            return agendamentoRepository.findByServicoProfissionalId(usuario.getId());
        } else {
            return agendamentoRepository.findByClienteId(usuario.getId());
        }
    }

    public Agendamento atualizarStatus(UUID id, StatusAgendamento novoStatus, Usuario profissionalLogado) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        // Debug no Terminal para entendermos o erro
        System.out.println(">>> TENTATIVA DE ATUALIZAR STATUS <<<");
        System.out.println("Profissional Logado ID: " + profissionalLogado.getId());
        System.out.println("Dono do Serviço ID: " + agendamento.getServico().getProfissional().getId());

        // Usamos toString() para garantir que a comparação funciona mesmo se forem objetos diferentes
        String idLogado = profissionalLogado.getId().toString();
        String idDono = agendamento.getServico().getProfissional().getId().toString();

        if (!idDono.equals(idLogado)) {
            System.out.println(">>> ERRO: IDs não batem! Acesso negado.");
            throw new RuntimeException("Acesso negado: Você não é o dono deste serviço.");
        }

        agendamento.setStatus(novoStatus);
        return agendamentoRepository.save(agendamento);
    }

    // --- NOVO MÉTODO: GERAR DADOS DO DASHBOARD (ADICIONADO AQUI NO FINAL) ---
    public DashboardStatsDTO getDashboardStats(Usuario profissional) {
        // 1. Faturação
        BigDecimal faturamento = agendamentoRepository.calcularFaturamentoTotal(profissional.getId());

        // 2. Total de Clientes
        Long clientes = agendamentoRepository.contarClientesUnicos(profissional.getId());

        // 3. Agendamentos Hoje (Define o intervalo de 00:00 até 23:59 de hoje)
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime fimDia = LocalDate.now().atTime(LocalTime.MAX);
        
        Long hoje = agendamentoRepository.contarAgendamentosNoPeriodo(profissional.getId(), inicioDia, fimDia);

        return new DashboardStatsDTO(faturamento, hoje, clientes);
    }
}