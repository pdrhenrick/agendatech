package br.com.pedrohenrick.agendatech.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal; // Usamos BigDecimal para valores monetários
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_SERVICOS")
public class Servico implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private Integer duracaoMinutos; // Ex: 30 (para 30 minutos)

    @Column(nullable = false)
    private BigDecimal preco; // Ex: 50.00

    // Esta é a "chave estrangeira" (Foreign Key)
    @ManyToOne // "Muitos" serviços podem pertencer a "Um" profissional
    @JoinColumn(name = "profissional_id", nullable = false) // Define o nome da coluna no banco
    private Usuario profissional; // A classe Servico se relaciona com a classe Usuario
}