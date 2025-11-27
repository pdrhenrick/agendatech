package br.com.pedrohenrick.agendatech.service;

import br.com.pedrohenrick.agendatech.model.Servico;
import br.com.pedrohenrick.agendatech.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public Servico criarServico(Servico servico) {
        return servicoRepository.save(servico);
    }

    public void excluirServico(UUID id) {
        if (servicoRepository.existsById(id)) {
            servicoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Serviço não encontrado com ID: " + id);
        }
    }
    
    // --- NOVO: Método para Atualizar ---
    public Servico atualizarServico(UUID id, Servico servicoAtualizado) {
        Servico servicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        
        // Atualiza os dados
        servicoExistente.setNome(servicoAtualizado.getNome());
        servicoExistente.setPreco(servicoAtualizado.getPreco());
        servicoExistente.setDuracaoMinutos(servicoAtualizado.getDuracaoMinutos());
        
        return servicoRepository.save(servicoExistente);
    }
    
    // --- NOVO: Método para buscar UM serviço (útil para preencher o formulário de edição) ---
    public Servico buscarPorId(UUID id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
    }
}