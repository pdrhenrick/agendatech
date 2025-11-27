import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Dashboard() {
    const navigate = useNavigate();
    
    // Estados para guardar os números
    const [stats, setStats] = useState({
        faturamentoTotal: 0,
        agendamentosHoje: 0,
        totalClientes: 0
    });
    
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem('authToken');
        const role = localStorage.getItem('userRole');

        // 1. Segurança: Só Profissionais entram aqui
        if (!token || role !== 'PROFISSIONAL') {
            alert("Acesso restrito a profissionais.");
            navigate('/servicos'); // Manda para uma página segura
            return;
        }

        const fetchDashboard = async () => {
            try {
                const headers = { 'Authorization': `Bearer ${token}` };
                // 2. Busca os dados do endpoint novo
                const response = await axios.get('http://localhost:8081/agendamentos/dashboard', { headers });
                setStats(response.data);
            } catch (err) {
                console.error("Erro ao carregar dashboard:", err);
                setError("Não foi possível carregar os dados.");
            } finally {
                setLoading(false);
            }
        };

        fetchDashboard();
    }, [navigate]);

    if (loading) return <p style={{padding: '20px'}}>Calculando métricas...</p>;
    if (error) return <p style={{ padding: '20px', color: 'red' }}>{error}</p>;

    return (
        <div style={{ padding: '20px', maxWidth: '1000px', margin: '0 auto' }}>
            <h2 style={{ marginBottom: '30px', color: '#333' }}>📊 Painel de Controle</h2>

            {/* Container dos Cards */}
            <div style={{ 
                display: 'flex', 
                gap: '20px', 
                flexWrap: 'wrap', 
                justifyContent: 'space-between' 
            }}>
                
                {/* CARD 1: FATURAMENTO */}
                <div style={cardStyle('#d1e7dd', '#0f5132')}>
                    <h3 style={{ margin: 0, fontSize: '18px' }}>Faturamento Total</h3>
                    <p style={{ fontSize: '32px', fontWeight: 'bold', margin: '10px 0' }}>
                        R$ {stats.faturamentoTotal.toFixed(2)}
                    </p>
                    <small>Soma de serviços concluídos</small>
                </div>

                {/* CARD 2: HOJE */}
                <div style={cardStyle('#fff3cd', '#664d03')}>
                    <h3 style={{ margin: 0, fontSize: '18px' }}>Agendamentos Hoje</h3>
                    <p style={{ fontSize: '32px', fontWeight: 'bold', margin: '10px 0' }}>
                        {stats.agendamentosHoje}
                    </p>
                    <small>Atendimentos para o dia atual</small>
                </div>

                {/* CARD 3: CLIENTES */}
                <div style={cardStyle('#cfe2ff', '#084298')}>
                    <h3 style={{ margin: 0, fontSize: '18px' }}>Total de Clientes</h3>
                    <p style={{ fontSize: '32px', fontWeight: 'bold', margin: '10px 0' }}>
                        {stats.totalClientes}
                    </p>
                    <small>Clientes únicos atendidos</small>
                </div>
            </div>

            <div style={{ marginTop: '40px', padding: '20px', background: '#f8f9fa', borderRadius: '10px' }}>
                <h4>🚀 Dica do Dia</h4>
                <p>Mantenha os status dos seus agendamentos atualizados para que o faturamento reflita a realidade!</p>
            </div>
        </div>
    );
}

// Estilo reutilizável para os cards
const cardStyle = (bgColor, textColor) => ({
    flex: '1 1 250px', // Cresce e encolhe, base 250px
    padding: '20px',
    borderRadius: '10px',
    backgroundColor: bgColor,
    color: textColor,
    boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
    textAlign: 'center'
});

export default Dashboard;