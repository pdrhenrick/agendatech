package br.com.pedrohenrick.agendatech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data // Anotação do Lombok (cria getters, setters, etc.)
public class LoginRequestDTO {

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;
}