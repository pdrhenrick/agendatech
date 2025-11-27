package br.com.pedrohenrick.agendatech.service;

import br.com.pedrohenrick.agendatech.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    // CHAVE FORTE E FIXA (Para evitar erros de leitura do application.properties agora)
    private final String secret = "MinhaChaveMuitoSecretaAgendatech123456789"; 
    
    // Se não achar o issuer, usa "agendatech"
    @Value("${api.security.token.issuer:agendatech}")
    private String issuer;

    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("-03:00");

    public String generateToken(Usuario usuario) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Date expirationDate = Date.from(generateExpirationDate());

            return Jwts.builder()
                    .issuer(issuer)
                    .subject(usuario.getEmail())
                    .claim("role", usuario.getRole().toString()) 
                    .issuedAt(new Date())
                    .expiration(expirationDate)
                    .signWith(key)
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    public String validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            return Jwts.parser()
                    .verifyWith(key)
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            // O ESPIÃO: Se o token falhar, ele vai gritar no terminal!
            System.out.println(">>> TOKEN INVÁLIDO: " + e.getMessage());
            return "";
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZONE_OFFSET);
    }
}