package br.com.pedrohenrick.agendatech.dto;

// Importa as classes que vamos usar
import br.com.pedrohenrick.agendatech.enums.Role;
import jakarta.validation.constraints.Email; // Importa a validação de Email
import jakarta.validation.constraints.NotBlank; // Importa a validação "não pode ser nulo nem vazio"
import jakarta.validation.constraints.NotNull; // Importa a validação "não pode ser nulo"
import jakarta.validation.constraints.Size; // Importa a validação de tamanho
import lombok.Data;

@Data // Anotação do Lombok (cria getters, setters, etc.)
public class AuthRequestDTO {

    /**
     * Esta anotação (@NotBlank) faz duas coisas:
     * 1. O campo não pode ser nulo (null).
     * 2. O campo não pode ser vazio (ex: "").
     * Se a requisição falhar, ela retorna a 'message'.
     */
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido") // Valida se o texto parece um email
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") // Valida o tamanho
    private String senha;

    /**
     * Para Enums (como o Role), usamos @NotNull, 
     * pois @NotBlank só se aplica a Strings.
     */
    @NotNull(message = "O tipo (role) é obrigatório (CLIENTE ou PROFISSIONAL)")
    private Role role; // O usuário dirá se é CLIENTE ou PROFISSIONAL
}