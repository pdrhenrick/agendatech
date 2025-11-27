package br.com.pedrohenrick.agendatech.controller;

import br.com.pedrohenrick.agendatech.dto.ServicoRequestDTO;
import br.com.pedrohenrick.agendatech.model.Servico;
import br.com.pedrohenrick.agendatech.model.Usuario;
import br.com.pedrohenrick.agendatech.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public ResponseEntity<List<Servico>> listar() {
        return ResponseEntity.ok(servicoService.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicoService.buscarPorId(id));
    }

// Importe a classe Usuario se não estiver importada
    // import br.com.pedrohenrick.agendatech.model.Usuario;
    // import org.springframework.security.core.context.SecurityContextHolder;

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody ServicoRequestDTO dados) {
        try {
            // 1. Pega o "Crachá" de quem está logado
            var authentication = SecurityContextHolder.getContext().getAuthentication();

            // 2. Verifica se o usuário está logado mesmo
            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro: Você precisa estar logado para criar um serviço.");
            }

            // 3. Pega o Objeto Usuario de dentro do crachá
            Usuario profissionalLogado = (Usuario) authentication.getPrincipal();

            // 4. Cria o serviço e PREENCHE O DONO
            Servico novoServico = new Servico();
            novoServico.setNome(dados.getNome());
            novoServico.setPreco(dados.getPreco());
            novoServico.setDuracaoMinutos(dados.getDuracaoMinutos());
            
            // AQUI ESTÁ A CORREÇÃO DO ERRO 500:
            novoServico.setProfissional(profissionalLogado); 

            return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.criarServico(novoServico));
            
        } catch (Exception e) {
            // Se der erro, mostra no terminal para a gente ver
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        servicoService.excluirServico(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable UUID id, @RequestBody ServicoRequestDTO dados) {
        Servico servicoParaAtualizar = new Servico();
        servicoParaAtualizar.setNome(dados.getNome());
        servicoParaAtualizar.setPreco(dados.getPreco());
        servicoParaAtualizar.setDuracaoMinutos(dados.getDuracaoMinutos());
        
        return ResponseEntity.ok(servicoService.atualizarServico(id, servicoParaAtualizar));
    }
}