import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom'; // 1. Importamos useParams para pegar o ID da URL

function CriarServico() {
    const navigate = useNavigate();
    const { id } = useParams(); // 2. Pegamos o ID (se existir)

    // Estados para o formulário
    const [nome, setNome] = useState('');
    const [valor, setValor] = useState(''); 
    const [duracaoMinutos, setDuracaoMinutos] = useState('');

    const [mensagem, setMensagem] = useState('');
    const [loading, setLoading] = useState(false);
    const [isEditing, setIsEditing] = useState(false); // Novo estado para controlar o modo

    // --- EFEITO 1: SEGURANÇA E CARREGAMENTO DE DADOS (SE FOR EDIÇÃO) ---
    useEffect(() => {
        const token = localStorage.getItem('authToken');
        const role = localStorage.getItem('userRole')?.trim();

        // 1. Verificação de Segurança
        if (!token) {
            alert("Acesso negado. Faça o login.");
            navigate('/login'); 
            return;
        }

        if (role !== 'PROFISSIONAL') {
            alert("Acesso negado. Apenas profissionais podem gerenciar serviços.");
            navigate('/servicos'); 
            return;
        }

        // 2. Verificação de Edição: Se tiver ID, busca os dados!
        if (id) {
            setIsEditing(true); // Ativa modo edição
            carregarDadosServico(id, token);
        }
        
    }, [id, navigate]);

    // Função auxiliar para buscar os dados do serviço antigo
    const carregarDadosServico = async (servicoId, token) => {
        try {
            setLoading(true);
            const headers = { 'Authorization': `Bearer ${token}` };
            const response = await axios.get(`http://localhost:8081/servicos/${servicoId}`, { headers });
            
            const servico = response.data;
            // Preenche o formulário com os dados que vieram do banco
            setNome(servico.nome);
            setValor(servico.preco);
            setDuracaoMinutos(servico.duracaoMinutos);
            setLoading(false);
        } catch (error) {
            console.error("Erro ao carregar serviço:", error);
            alert("Erro ao carregar dados do serviço para edição.");
            navigate('/servicos'); // Volta se der erro
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMensagem('');

        const token = localStorage.getItem('authToken');

        const servicoData = {
            nome: nome,
            preco: parseFloat(valor),
            duracaoMinutos: parseInt(duracaoMinutos)
        };

        const headers = {
            'Authorization': `Bearer ${token}`
        };

        console.log(isEditing ? "Atualizando serviço..." : "Criando novo serviço...", servicoData);

        try {
            if (isEditing) {
                // ===========================================
                // MODO EDIÇÃO: Usa PUT e manda o ID na URL
                // ===========================================
                await axios.put(`http://localhost:8081/servicos/${id}`, servicoData, { headers });
                setMensagem('Serviço atualizado com sucesso!');
            } else {
                // ===========================================
                // MODO CRIAÇÃO: Usa POST normal
                // ===========================================
                await axios.post('http://localhost:8081/servicos', servicoData, { headers });
                setMensagem('Serviço criado com sucesso!');
            }

            setLoading(false);

            // Redireciona após 1.5 segundos
            setTimeout(() => {
                navigate('/servicos');
            }, 1500);

        } catch (error) {
            console.error("Erro ao salvar serviço:", error.response);
            const erroMsg = error.response?.data?.message || error.response?.data || error.message || 'Erro desconhecido';
            setMensagem(`Erro ao salvar: ${erroMsg}`);
            setLoading(false);
        }
    };

    return (
        <div>
            {/* Título dinâmico: Muda se for Edição ou Criação */}
            <h2>{isEditing ? 'Editar Serviço' : 'Criar Novo Serviço'}</h2>
            <p>{isEditing ? 'Altere os dados abaixo e salve.' : 'Preencha os dados para cadastrar um novo serviço.'}</p>

            <form onSubmit={handleSubmit}>
                <div style={{ margin: '10px 0' }}>
                    <label>Nome do Serviço: </label>
                    <input type="text" value={nome} onChange={(e) => setNome(e.target.value)} required />
                </div>
                <div style={{ margin: '10px 0' }}>
                    <label>Valor (R$): </label>
                    <input type="number" step="0.01" value={valor} onChange={(e) => setValor(e.target.value)} required />
                </div>
                <div style={{ margin: '10px 0' }}>
                    <label>Duração (minutos): </label>
                    <input type="number" value={duracaoMinutos} onChange={(e) => setDuracaoMinutos(e.target.value)} required />
                </div>

                <button type="submit" disabled={loading} style={{ 
                    padding: '10px 20px', 
                    cursor: 'pointer',
                    backgroundColor: isEditing ? '#007bff' : '#28a745', // Azul se editar, Verde se criar
                    color: 'white',
                    border: 'none',
                    borderRadius: '5px',
                    fontSize: '16px'
                }}>
                    {loading ? 'Salvando...' : (isEditing ? 'Atualizar Serviço' : 'Salvar Serviço')}
                </button>
            </form>

            {mensagem && <p style={{ marginTop: '20px', fontWeight: 'bold', color: mensagem.includes('Erro') ? 'red' : 'green' }}>{mensagem}</p>}
        </div>
    );
}

export default CriarServico;