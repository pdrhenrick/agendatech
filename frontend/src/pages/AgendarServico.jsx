import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function AgendarServico() {
    const navigate = useNavigate();

    // Estados para armazenar os dados
    const [servicos, setServicos] = useState([]); // Lista de serviços para o Select
    const [servicoId, setServicoId] = useState('');
    const [data, setData] = useState('');
    const [hora, setHora] = useState('');
    const [observacoes, setObservacoes] = useState('');

    const [mensagem, setMensagem] = useState('');
    const [loading, setLoading] = useState(false);

    // --- AO CARREGAR A TELA: BUSCA OS SERVIÇOS ---
    useEffect(() => {
        const token = localStorage.getItem('authToken');
        
        if (!token) {
            alert("Você precisa estar logado para agendar.");
            navigate('/login');
            return;
        }

        const fetchServicos = async () => {
            try {
                const headers = { 'Authorization': `Bearer ${token}` };
                const response = await axios.get('http://localhost:8081/servicos', { headers });
                setServicos(response.data);
            } catch (error) {
                console.error("Erro ao buscar serviços:", error);
                setMensagem("Erro ao carregar lista de serviços.");
            }
        };

        fetchServicos();
    }, [navigate]);

    // --- AO CLICAR EM "AGENDAR" ---
    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMensagem('');

        const token = localStorage.getItem('authToken');
        
        // 1. Formata a data para o padrão do Java (yyyy-MM-ddTHH:mm:ss)
        // Ex: Junta "2025-12-25" com "14:30" virando "2025-12-25T14:30:00"
        const dataHoraFormatada = `${data}T${hora}:00`;

        const agendamentoData = {
            servicoId: servicoId,
            dataHora: dataHoraFormatada,
            observacoes: observacoes
        };

        const headers = { 'Authorization': `Bearer ${token}` };

        try {
            await axios.post('http://localhost:8081/agendamentos', agendamentoData, { headers });
            
            setMensagem('Agendamento realizado com sucesso!');
            setLoading(false);
            
            // Limpa o formulário
            setServicoId('');
            setData('');
            setHora('');
            setObservacoes('');

        } catch (error) {
            console.error("Erro ao agendar:", error.response);
            // Pega a mensagem de erro que nosso Backend mandou (ex: "Horário ocupado")
            const erroMsg = error.response?.data || "Erro ao realizar agendamento.";
            setMensagem(`Erro: ${erroMsg}`);
            setLoading(false);
        }
    };

    return (
        <div style={{ maxWidth: '500px', margin: '0 auto' }}>
            <h2>Agendar Atendimento</h2>
            <p>Escolha o serviço e o melhor horário para você.</p>

            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                
                {/* 1. SELEÇÃO DE SERVIÇO */}
                <div>
                    <label>Escolha o Serviço:</label>
                    <select 
                        value={servicoId} 
                        onChange={(e) => setServicoId(e.target.value)} 
                        required
                        style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                    >
                        <option value="">Selecione...</option>
                        {servicos.map(servico => (
                            <option key={servico.id} value={servico.id}>
                                {servico.nome} - R$ {servico.preco} ({servico.duracaoMinutos} min)
                            </option>
                        ))}
                    </select>
                </div>

                {/* 2. DATA E HORA */}
                <div style={{ display: 'flex', gap: '10px' }}>
                    <div style={{ flex: 1 }}>
                        <label>Data:</label>
                        <input 
                            type="date" 
                            value={data} 
                            onChange={(e) => setData(e.target.value)} 
                            required 
                            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                        />
                    </div>
                    <div style={{ flex: 1 }}>
                        <label>Hora:</label>
                        <input 
                            type="time" 
                            value={hora} 
                            onChange={(e) => setHora(e.target.value)} 
                            required 
                            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                        />
                    </div>
                </div>

                {/* 3. OBSERVAÇÕES */}
                <div>
                    <label>Observações (Opcional):</label>
                    <textarea 
                        value={observacoes} 
                        onChange={(e) => setObservacoes(e.target.value)} 
                        placeholder="Ex: Tenho alergia a..."
                        rows="3"
                        style={{ width: '100%', padding: '8px', marginTop: '5px' }}
                    />
                </div>

                <button 
                    type="submit" 
                    disabled={loading}
                    style={{ 
                        padding: '12px', 
                        backgroundColor: '#007bff', 
                        color: 'white', 
                        border: 'none', 
                        borderRadius: '5px', 
                        fontSize: '16px', 
                        cursor: 'pointer' 
                    }}
                >
                    {loading ? 'Agendando...' : 'Confirmar Agendamento'}
                </button>
            </form>

            {mensagem && (
                <div style={{ 
                    marginTop: '20px', 
                    padding: '10px', 
                    borderRadius: '5px',
                    backgroundColor: mensagem.includes('Erro') ? '#f8d7da' : '#d4edda',
                    color: mensagem.includes('Erro') ? '#721c24' : '#155724',
                    border: mensagem.includes('Erro') ? '1px solid #f5c6cb' : '1px solid #c3e6cb'
                }}>
                    {mensagem}
                </div>
            )}
        </div>
    );
}

export default AgendarServico;