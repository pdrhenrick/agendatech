import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function MeusAgendamentos() {
    const navigate = useNavigate();
    
    const [agendamentos, setAgendamentos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [userRole, setUserRole] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem('authToken');
        const role = localStorage.getItem('userRole');
        setUserRole(role);

        if (!token) {
            alert("Acesso negado. Faça login.");
            navigate('/login');
            return;
        }
        carregarAgenda();
    }, [navigate]);

    const carregarAgenda = async () => {
        const token = localStorage.getItem('authToken');
        try {
            const headers = { 'Authorization': `Bearer ${token}` };
            const response = await axios.get('http://localhost:8081/agendamentos/meus', { headers });
            setAgendamentos(response.data);
        } catch (err) {
            console.error("Erro ao buscar agendamentos:", err);
            setError("Não foi possível carregar sua agenda.");
        } finally {
            setLoading(false);
        }
    };

    const handleStatusChange = async (id, novoStatus) => {
        if (!window.confirm(`Mudar status para ${novoStatus}?`)) return;
        const token = localStorage.getItem('authToken');
        try {
            const headers = { 'Authorization': `Bearer ${token}` };
            await axios.put(`http://localhost:8081/agendamentos/${id}/status`, { status: novoStatus }, { headers });
            carregarAgenda();
        } catch (err) {
            alert("Erro: " + (err.response?.data || "Falha ao atualizar"));
        }
    };

    const formatarData = (dataString) => {
        return new Date(dataString).toLocaleString('pt-BR', {
            day: '2-digit', month: '2-digit', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    };

    // Cores das Pílulas de Status
    const getStatusStyle = (status) => {
        const styles = {
            'PENDENTE':   { bg: '#fffbeb', color: '#b45309' }, // Amarelo
            'CONFIRMADO': { bg: '#ecfdf5', color: '#047857' }, // Verde
            'CANCELADO':  { bg: '#fef2f2', color: '#b91c1c' }, // Vermelho
            'CONCLUIDO':  { bg: '#eff6ff', color: '#1d4ed8' }, // Azul
        };
        return styles[status] || { bg: '#f3f4f6', color: '#374151' };
    };

    if (loading) return <p>Carregando...</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return (
        <div>
            <h2 style={{ marginBottom: '20px', color: '#1f2937' }}>📅 Minha Agenda</h2>

            {agendamentos.length === 0 ? (
                <div className="table-container" style={{ padding: '3rem', textAlign: 'center', color: '#6b7280' }}>
                    <p>Nenhum agendamento encontrado.</p>
                </div>
            ) : (
                <div className="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Data / Hora</th>
                                <th>Serviço</th>
                                <th>{userRole === 'PROFISSIONAL' ? 'Cliente' : 'Profissional'}</th>
                                <th>Status</th>
                                {userRole === 'PROFISSIONAL' && <th>Ações</th>}
                            </tr>
                        </thead>
                        <tbody>
                            {agendamentos.map((item) => {
                                const statusStyle = getStatusStyle(item.status);
                                return (
                                    <tr key={item.id}>
                                        <td>{formatarData(item.dataHora)}</td>
                                        <td>
                                            <div style={{ fontWeight: 'bold' }}>{item.servico.nome}</div>
                                            <div style={{ fontSize: '0.85rem', color: '#6b7280' }}>R$ {item.servico.preco}</div>
                                        </td>
                                        <td>
                                            {userRole === 'PROFISSIONAL' ? item.cliente.nome : item.servico.profissional.nome}
                                        </td>
                                        <td>
                                            <span className="status-badge" style={{ backgroundColor: statusStyle.bg, color: statusStyle.color }}>
                                                {item.status}
                                            </span>
                                        </td>
                                        
                                        {/* AÇÕES (Botões Redondos) */}
                                        {userRole === 'PROFISSIONAL' && (
                                            <td>
                                                <div style={{ display: 'flex', gap: '8px' }}>
                                                    {item.status === 'PENDENTE' && (
                                                        <>
                                                            <button className="btn-success" onClick={() => handleStatusChange(item.id, 'CONFIRMADO')}>
                                                                Confirmar
                                                            </button>
                                                            <button className="btn-danger" onClick={() => handleStatusChange(item.id, 'CANCELADO')}>
                                                                Recusar
                                                            </button>
                                                        </>
                                                    )}
                                                    {item.status === 'CONFIRMADO' && (
                                                        <>
                                                            <button className="btn-primary" onClick={() => handleStatusChange(item.id, 'CONCLUIDO')}>
                                                                Concluir
                                                            </button>
                                                            <button className="btn-danger" onClick={() => handleStatusChange(item.id, 'CANCELADO')}>
                                                                Cancelar
                                                            </button>
                                                        </>
                                                    )}
                                                </div>
                                            </td>
                                        )}
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}

export default MeusAgendamentos;