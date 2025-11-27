package br.com.pedrohenrick.agendatech.controller;

import br.com.pedrohenrick.agendatech.dto.AgendamentoRequestDTO;
import br.com.pedrohenrick.agendatech.dto.DashboardStatsDTO; // <--- NOVO IMPORT
import br.com.pedrohenrick.agendatech.enums.Role; // <--- RECOMENDADO: Importar o Role para limpar o código
import br.com.pedrohenrick.agendatech.enums.StatusAgendamento;
import br.com.pedrohenrick.agendatech.model.Agendamento;
import br.com.pedrohenrick.agendatech.model.Usuario;
import br.com.pedrohenrick.agendatech.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody AgendamentoRequestDTO dados) {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario clienteLogado = (Usuario) auth.getPrincipal();

            Agendamento agendamento = agendamentoService.criarAgendamento(dados, clienteLogado);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(agendamento);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao agendar: " + e.getMessage());
        }
    }

    @GetMapping("/meus")
    public ResponseEntity<List<Agendamento>> listarMeusAgendamentos() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioLogado = (Usuario) auth.getPrincipal();

            List<Agendamento> lista = agendamentoService.listarPorUsuario(usuarioLogado);
            
            return ResponseEntity.ok(lista);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Recebe um JSON assim: { "status": "CONFIRMADO" }
    @PutMapping("/{id}/status")
    public ResponseEntity<Object> atualizarStatus(@PathVariable UUID id, @RequestBody Map<String, String> payload) {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario profissionalLogado = (Usuario) auth.getPrincipal();

            String statusString = payload.get("status");
            // Converte a String para o Enum (Ex: "CONFIRMADO" -> StatusAgendamento.CONFIRMADO)
            StatusAgendamento novoStatus = StatusAgendamento.valueOf(statusString);

            Agendamento atualizado = agendamentoService.atualizarStatus(id, novoStatus, profissionalLogado);
            
            return ResponseEntity.ok(atualizado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Status inválido.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar.");
        }
    }

    // --- NOVO ENDPOINT: DASHBOARD (ADICIONADO AQUI NO FINAL) ---
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboard() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario profissionalLogado = (Usuario) auth.getPrincipal();

            // Segurança extra: Só profissional pode ver dashboard
            // Obs: Adicionei o import de Role lá em cima para ficar mais limpo aqui
            if (profissionalLogado.getRole() != Role.PROFISSIONAL) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            DashboardStatsDTO stats = agendamentoService.getDashboardStats(profissionalLogado);
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}