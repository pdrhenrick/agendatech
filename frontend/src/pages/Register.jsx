import React, { useState } from 'react'; // Importa o React e a ferramenta 'useState' (para guardar dados)
import axios from 'axios'; // Importa o "mensageiro"
import { useNavigate } from 'react-router-dom'; // Importa a ferramenta de navegação

// Este é o nosso componente (nossa página)
function Register() {
    // --- O "Estado" (State) ---
    // Aqui criamos "caixinhas" para guardar o que o usuário digita
    const [nome, setNome] = useState(''); // Caixinha para o nome
    const [email, setEmail] = useState(''); // Caixinha para o email
    const [senha, setSenha] = useState(''); // Caixinha para a senha
    const [role, setRole] = useState('CLIENTE'); // Caixinha para o tipo (começa como CLIENTE)

    // --- Estado para Mensagens ---
    // Caixinha para guardar mensagens de sucesso ou erro
    const [mensagem, setMensagem] = useState(''); 

    // Inicializa a ferramenta de navegação
    const navigate = useNavigate();

    // --- A Função de Envio ---
    // Esta função será chamada quando o usuário clicar no botão "Registrar"
    const handleSubmit = async (e) => {
        // e.preventDefault() -> Impede o navegador de recarregar a página
        e.preventDefault();

        // Limpa a mensagem anterior antes de tentar de novo
        setMensagem('');

        // 1. Junta todos os dados das "caixinhas" em um objeto
        const data = {
            nome: nome,
            email: email,
            senha: senha,
            role: role
        };

        console.log("Enviando dados para o backend:", data);

        try {
            // 2. Chama o "mensageiro" (Axios) para fazer o POST
            // O Axios vai bater lá no nosso Backend Java (na porta 8081)
            const response = await axios.post('http://localhost:8081/auth/register', data);
            
            // 3. Se deu certo (201 Created), mostramos no console
            console.log("Resposta do backend:", response.data);
            
            // Define a mensagem de sucesso
            setMensagem("Usuário registrado com sucesso! Redirecionando para o login...");

            // 4. Navega o usuário para a página de Login após 2 segundos
            setTimeout(() => {
                navigate('/login'); // Usa o navigate para mudar de página
            }, 2000); // 2000ms = 2 segundos

        } catch (error) {
            // 5. Se deu erro (400, 500, etc.)
            console.error("Erro ao registrar:", error.response.data);
            
            // Define a mensagem de erro que veio do Backend
            // (Ex: "Email já cadastrado")
            setMensagem("Erro ao registrar: " + error.response.data);
        }
    };

    // --- O Visual (HTML/JSX) ---
    // O que a página vai mostrar
    return (
        <div>
            <h2>Página de Registro</h2>
            {/* Quando o formulário for enviado, ele chama a função 'handleSubmit' */}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Nome:</label>
                    <input 
                        type="text" 
                        value={nome} // O valor do input vem da "caixinha" 'nome'
                        onChange={(e) => setNome(e.target.value)} // Quando o usuário digita, atualiza a "caixinha" 'nome'
                        required 
                    />
                </div>
                <div>
                    <label>Email:</label>
                    <input 
                        type="email" 
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required 
                    />
                </div>
                <div>
                    <label>Senha:</label>
                    <input 
                        type="password" 
                        value={senha}
                        onChange={(e) => setSenha(e.target.value)}
                        required 
                        minLength={6} // Validação do HTML
                    />
                </div>
                <div>
                    <label>Tipo de Conta:</label>
                    <select value={role} onChange={(e) => setRole(e.target.value)}>
                        <option value="CLIENTE">Cliente</option>
                        <option value="PROFISSIONAL">Profissional</option>
                    </select>
                </div>
                <button type="submit">Registrar</button>
            </form>

            {/* --- Área de Mensagem --- */}
            {/* Se a "caixinha" 'mensagem' não estiver vazia, mostre ela aqui */}
            {mensagem && <p>{mensagem}</p>}
        </div>
    );
}

export default Register;