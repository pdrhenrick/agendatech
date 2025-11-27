package br.com.pedrohenrick.agendatech.repository;

import br.com.pedrohenrick.agendatech.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
// Gerencia a entidade 'Servico', cujo ID é do tipo 'UUID'
public interface ServicoRepository extends JpaRepository<Servico, UUID> {

    /**
     * O Spring Data JPA cria este método automaticamente para nós.
     * Ele vai gerar o SQL: "SELECT * FROM TB_SERVICOS WHERE profissional_id = ?"
     */
    List<Servico> findByProfissionalId(UUID profissionalId);

}