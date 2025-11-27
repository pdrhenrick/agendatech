package br.com.pedrohenrick.agendatech.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Anotação do Lombok (cria getters, setters, etc.)
@NoArgsConstructor // Cria um construtor vazio (necessário para o Spring)
public class LoginResponseDTO {

    private String token;

    // Construtor que recebe o token
    public LoginResponseDTO(String token) {
        this.token = token;
    }
}