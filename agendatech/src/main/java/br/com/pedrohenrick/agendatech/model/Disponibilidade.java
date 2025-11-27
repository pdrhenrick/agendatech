package br.com.pedrohenrick.agendatech.model;

import br.com.pedrohenrick.agendatech.enums.DiaDaSemana;
import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalTime; // Usamos LocalTime para guardar só a hora (ex: 09:00)
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_DISPONIBILIDADE")
public class Disponibilidade implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiaDaSemana diaDaSemana; // Ex: SEGUNDA

    @Column(nullable = false)
    private LocalTime horaInicio; // Ex: 09:00:00

    @Column(nullable = false)
    private LocalTime horaFim; // Ex: 18:00:00

    @ManyToOne // "Muitas" disponibilidades pertencem a "Um" profissional
    @JoinColumn(name = "profissional_id", nullable = false)
    private Usuario profissional;
}