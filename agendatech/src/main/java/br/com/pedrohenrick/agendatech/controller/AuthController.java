package br.com.pedrohenrick.agendatech.controller;

import br.com.pedrohenrick.agendatech.dto.AuthRequestDTO;
import br.com.pedrohenrick.agendatech.dto.LoginRequestDTO; // <-- NOVA IMPORTAÇÃO
import br.com.pedrohenrick.agendatech.dto.LoginResponseDTO; // <-- NOVA IMPORTAÇÃO
import br.com.pedrohenrick.agendatech.model.Usuario;
import br.com.pedrohenrick.agendatech.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// =======================================================
// ===== NOVAS IMPORTAÇÕES PARA O LOGIN =====
// =======================================================
import org.springframework.security.core.AuthenticationException;
// =======================================================
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint 1: REGISTRO
     * URL: POST http://localhost:8080/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        try {
            Usuario novoUsuario = authService.register(authRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // =======================================================
    // ===== NOVO ENDPOINT DE LOGIN ADICIONADO =====
    // =======================================================
    /**
     * Endpoint 2: LOGIN
     * URL: POST http://localhost:8080/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            // 1. Tenta autenticar e gerar o token
            String token = authService.login(loginRequestDTO);
            
            // 2. Se der certo, retorna o token dentro do nosso DTO de resposta
            //    e o Status HTTP 200 (OK)
            return ResponseEntity.ok(new LoginResponseDTO(token));

        } catch (AuthenticationException e) {
            // 3. Se o 'authenticationManager' falhar (email não existe, senha errada),
            //    ele lança uma 'AuthenticationException'.
            
            // 4. Retornamos um erro HTTP 401 (UNAUTHORIZED)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login falhou: Email ou senha inválidos.");
        }
    }
}