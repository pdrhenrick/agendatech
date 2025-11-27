import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode'; // <-- IMPORTA O "LEITOR DE CRACHÁS"

function Login() {
    // --- O "Estado" (State) ---
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [mensagem, setMensagem] = useState('');
    const navigate = useNavigate();

    // --- A Função de Envio ---
    const handleSubmit = async (e) => {
        e.preventDefault(); 
        setMensagem('');

        const data = {
            email: email,
            senha: senha
        };

        console.log("Enviando dados de login:", data);

        try {
            // 1. Chama o "mensageiro" (Axios)
            const response = await axios.post('http://localhost:8081/auth/login', data);
            
            // 2. Se deu certo (200 OK), o token estará em 'response.data.token'
            const token = response.data.token;
            console.log("Resposta do backend (Token):", token);
            
            // 3. "Lemos" o token (o "crachá")
            const decodedToken = jwtDecode(token);

            // 4. Tiramos o "cargo" de dentro do crachá
            const userRole = decodedToken.role; 
            console.log("Cargo (Role) lido do token:", userRole); // Ex: "ROLE_PROFISSIONAL"

            // 5. Guardamos as DUAS coisas no "cofre" do navegador
            localStorage.setItem('authToken', token);
            localStorage.setItem('userRole', userRole); // <-- A CHAVE QUE FALTAVA!

            // 6. Mostra a mensagem de sucesso
            setMensagem("Login realizado com sucesso! Redirecionando...");

            // 7. Redireciona o usuário (para o /servicos, que é mais útil)
            setTimeout(() => {
                navigate('/servicos'); // <-- VAMOS MUDAR DE /dashboard PARA /servicos
            }, 1500); 

        } catch (error) {
            // Bloco de 'catch' melhorado
            if (error.response) {
                console.error("Erro de login:", error.response.data);
                setMensagem("Erro ao logar: " + error.response.data); 
            } else if (error.request) {
                console.error("Erro de rede:", error.message);
                setMensagem("Não foi possível conectar ao servidor.");
            } else {
                console.error("Erro:", error.message);
                setMensagem("Ocorreu um erro inesperado.");
            }
        }
    };

    // --- O Visual (HTML/JSX) ---
    return (
        <div>
            <h2>Página de Login</h2>
            <form onSubmit={handleSubmit}>
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
                    />
                </div>
                <button type="submit">Entrar</button>
            </form>

            {mensagem && <p>{mensagem}</p>}
        </div>
    );
}

// =======================================================
// ===== AQUI ESTÁ A "LINHA MÁGICA" QUE FALTAVA =====
// =======================================================
export default Login;