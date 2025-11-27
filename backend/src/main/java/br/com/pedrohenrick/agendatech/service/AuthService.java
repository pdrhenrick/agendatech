package br.com.pedrohenrick.agendatech.service;

import br.com.pedrohenrick.agendatech.dto.AuthRequestDTO;
import br.com.pedrohenrick.agendatech.dto.LoginRequestDTO; // <-- NOVA IMPORTAÇÃO
import br.com.pedrohenrick.agendatech.model.Usuario;
import br.com.pedrohenrick.agendatech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
// =======================================================
// ===== NOVAS IMPORTAÇÕES PARA O LOGIN =====
// =======================================================
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
// =======================================================
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service 
public class AuthService {

    // --- Injeção de Dependência ---
    
    @Autowired 
    private UsuarioRepository usuarioRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    // =======================================================
    // ===== NOVAS INJEÇÕES DE DEPENDÊNCIA =====
    // =======================================================
    @Autowired
    private AuthenticationManager authenticationManager; // O Gerenciador do Spring Security

    @Autowired
    private TokenService tokenService; // Nosso serviço de criar "passaportes"
    // =======================================================


    /**
     * Método 1: REGISTRO DE USUÁRIO (Já tínhamos)
     */
    public Usuario register(AuthRequestDTO authRequestDTO) {
        
        if (usuarioRepository.existsByEmail(authRequestDTO.getEmail())) {
            throw new RuntimeException("Erro: Email já cadastrado!");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(authRequestDTO.getNome());
        novoUsuario.setEmail(authRequestDTO.getEmail());
        novoUsuario.setRole(authRequestDTO.getRole());

        String senhaCriptografada = passwordEncoder.encode(authRequestDTO.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        return usuarioRepository.save(novoUsuario);
    }


    // =======================================================
    // ===== NOVO MÉTODO DE LOGIN ADICIONADO =====
    // =======================================================
    /**
     * Método 2: LOGIN DE USUÁRIO
     * Recebe o "formulário" de login (email e senha pura)
     * Retorna o Token (passaporte) se o login for válido.
     */
    public String login(LoginRequestDTO loginRequestDTO) {
        
        // 1. Cria um "pacote" de autenticação com o email e a senha pura
        // O Spring usa isso para comparar com o que está no banco.
        var usernamePassword = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getSenha()
        );

        // 2. Tenta autenticar
        // O 'authenticationManager' vai:
        //    a) Buscar o usuário no banco pelo email (através de um serviço interno do Spring)
        //    b) Pegar a senha criptografada do banco
        //    c) Comparar a senha criptografada com a senha pura (loginRequestDTO.getSenha())
        //    d) Se as senhas baterem, ele retorna um objeto 'Authentication'
        //    e) Se não baterem, ele lança uma exceção (que o Controller vai pegar)
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Se a autenticação deu certo (passou da linha acima), pegamos o usuário
        Usuario usuarioAutenticado = (Usuario) auth.getPrincipal();

        // 4. Geramos o "passaporte" (Token JWT) para esse usuário
        return tokenService.generateToken(usuarioAutenticado);
    }
}