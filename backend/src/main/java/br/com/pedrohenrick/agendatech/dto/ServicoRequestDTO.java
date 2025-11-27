package br.com.pedrohenrick.agendatech.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ServicoRequestDTO {
    private String nome;
    private BigDecimal preco;
    private Integer duracaoMinutos;
}