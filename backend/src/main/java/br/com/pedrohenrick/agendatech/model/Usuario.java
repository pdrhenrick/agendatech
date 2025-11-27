package br.com.pedrohenrick.agendatech.model;

import br.com.pedrohenrick.agendatech.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Collection; // <-- NOVA IMPORTAÇÃO
import java.util.List; // <-- NOVA IMPORTAÇÃO
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority; // <-- NOVA IMPORTAÇÃO
import org.springframework.security.core.authority.SimpleGrantedAuthority; // <-- NOVA IMPORTAÇÃO
import org.springframework.security.core.userdetails.UserDetails; // <-- NOVA IMPORTAÇÃO

@Data
@Entity
@Table(name = "TB_USUARIOS")
// 1. MUDANÇA AQUI: Adicionamos a interface 'UserDetails'
public class Usuario implements Serializable, UserDetails {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;

    // =======================================================
    // ===== 2. MUDANÇA AQUI: NOVOS MÉTODOS DA INTERFACE 'UserDetails' =====
    // =======================================================
    
@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 1. Debug para sabermos que o código novo carregou
        System.out.println("--- DEBUG DO USUARIO (NOVO) ---");
        System.out.println("Validando email: " + this.email);
        System.out.println("Role original do banco: " + this.role);

        // 2. Proteção contra erro se a role estiver vazia
        if (this.role == null) {
            System.out.println("ALERTA: Role nula! Retornando permissão de Cliente.");
            return List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"));
        }

        // 3. Converte para String (ex: "PROFISSIONAL")
        String nomeRole = this.role.toString(); 

        // 4. A SOLUÇÃO FINAL:
        // Retornamos UMA lista contendo AS DUAS versões do nome.
        // Assim, se o controller pedir 'ROLE_PROFISSIONAL' ou 'PROFISSIONAL', ambos funcionam.
        return List.of(
            new SimpleGrantedAuthority("ROLE_" + nomeRole), // Padrão Spring
            new SimpleGrantedAuthority(nomeRole)            // Padrão do seu Banco
        );
    }

    @Override
    public String getPassword() {
        // O Spring Security vai chamar este método para pegar a senha criptografada
        return this.senha;
    }

    @Override
    public String getUsername() {
        // O Spring Security vai chamar este método para pegar o "login" (que é o email)
        return this.email;
    }

    // --- Para este projeto, vamos deixar os outros como 'true' ---

    @Override
    public boolean isAccountNonExpired() {
        return true; // A conta não expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // A conta não está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // As credenciais (senha) não expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // A conta está habilitada
    }
}