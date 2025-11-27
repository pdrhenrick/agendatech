package br.com.pedrohenrick.agendatech.dto;

import br.com.pedrohenrick.agendatech.model.Servico;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor // Cria um construtor padrão (sem argumentos)
public class ServicoResponseDTO {

    private UUID id;
    private String nome;
    private String descricao;
    private Integer duracaoMinutos;
    private BigDecimal preco;

    /**
     * Este construtor é um "atalho".
     * Ele permite que a gente crie um DTO de Resposta
     * passando diretamente a Entidade 'Servico' que veio do banco.
     * Ex: new ServicoResponseDTO(servicoSalvo)
     */
    public ServicoResponseDTO(Servico servico) {
        this.id = servico.getId();
        this.nome = servico.getNome();
        this.descricao = servico.getDescricao();
        this.duracaoMinutos = servico.getDuracaoMinutos();
        this.preco = servico.getPreco();
    }
}