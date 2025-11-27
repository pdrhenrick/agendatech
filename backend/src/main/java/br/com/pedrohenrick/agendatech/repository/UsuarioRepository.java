package br.com.pedrohenrick.agendatech.repository;

// Importa as classes que vamos usar
import br.com.pedrohenrick.agendatech.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository // Esta anotação diz ao Spring que esta é uma interface de Repositório
            // (É opcional, mas uma boa prática para clareza)
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    // Nós estendemos 'JpaRepository', que já vem com métodos prontos
    // como save(), findById(), findAll(), delete(), etc.
    //
    // <Usuario, UUID> -> Significa que este repositório vai gerenciar
    // a entidade 'Usuario', e o tipo da chave primária (ID) é 'UUID'.

    /**
     * O Spring Data JPA é inteligente. Ao criar um método com este nome,
     * ele automaticamente cria a query SQL:
     * "SELECT * FROM TB_USUARIOS WHERE email = ?"
     * * 'Optional<Usuario>' -> Significa que ele pode ou não encontrar um usuário.
     * Isso nos protege contra erros de "null pointer".
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Da mesma forma, este método é criado automaticamente:
     * "SELECT COUNT(*) > 0 FROM TB_USUARIOS WHERE email = ?"
     * * É uma forma muito eficiente de apenas checar se um email já existe,
     * sem precisar trazer o objeto 'Usuario' inteiro do banco.
     */
    boolean existsByEmail(String email);
}