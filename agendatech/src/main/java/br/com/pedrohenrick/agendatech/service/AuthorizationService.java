package br.com.pedrohenrick.agendatech.service;

import br.com.pedrohenrick.agendatech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Marca esta classe como um "Serviço" gerenciado pelo Spring
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Este é o método que o Spring Security (AuthenticationManager)
     * vai chamar automaticamente quando tentarmos fazer login (no AuthService).
     *
     * @param email (O 'username' que vem do login)
     * @return UserDetails (O nosso objeto 'Usuario', que agora implementa 'UserDetails')
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Usa o nosso repositório para buscar o usuário pelo email
        UserDetails usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
        
        // 2. Retorna o usuário encontrado.
        // O Spring Security vai então pegar a senha deste objeto (usando o getPassword())
        // e comparar com a senha que o usuário digitou no login.
        return usuario;
    }
}