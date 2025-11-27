package br.com.pedrohenrick.agendatech.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test") // A URL base será http://localhost:8080/test
public class TestController {

    /**
     * Este endpoint (GET /test) NÃO está em "/auth/**".
     * De acordo com o nosso SecurityConfig, ele deve ser PROTEGIDO.
     * Só quem tiver um "passaporte" (Token JWT) válido poderá acessá-lo.
     */
    @GetMapping
    public ResponseEntity<String> getTest() {
        // Se o usuário chegou aqui, é porque o SecurityFilter validou o token
        // e o Spring Security o autenticou.
        return ResponseEntity.ok("SUCESSO! Você está autenticado e acessou uma rota protegida!");
    }
}