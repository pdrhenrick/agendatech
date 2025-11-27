import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

function Servicos() {
    const navigate = useNavigate();
    
    const [servicos, setServicos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [userRole, setUserRole] = useState(null);

    const getToken = () => localStorage.getItem('authToken');

    useEffect(() => {
        const token = getToken();
        const role = localStorage.getItem('userRole');

        if (!token) {
            alert("Acesso negado. Por favor, faça o login.");
            navigate('/login'); 
            return; 
        }

        setUserRole(role); 

        const fetchServicos = async () => {
            try {
                const headers = { 'Authorization': `Bearer ${token}` };
                const response = await axios.get('http://localhost:8081/servicos', { headers });
                setServicos(response.data);
            } catch (err) {
                console.error("Erro ao buscar serviços:", err);
                setError("Não foi possível carregar os serviços.");
            } finally {
                setLoading(false);
            }
        };

        fetchServicos(); 

    }, [navigate]); 

    const handleDelete = async (id) => {
        const confirm = window.confirm("Tem certeza que deseja excluir este serviço?");
        if (!confirm) return;

        try {
            const token = getToken();
            const headers = { 'Authorization': `Bearer ${token}` };
            
            await axios.delete(`http://localhost:8081/servicos/${id}`, { headers });
            
            setServicos(servicos.filter(servico => servico.id !== id));
            alert("Serviço excluído com sucesso!");

        } catch (err) {
            console.error("Erro ao excluir:", err);
            alert("Erro ao excluir. Verifique se o Backend suporta a função DELETE.");
        }
    };

    if (loading) return <p>Carregando serviços...</p>;
    if (error) return <p style={{ color: 'red' }}>{error}</p>;

    return (
        <div>
            <h2>Lista de Serviços Disponíveis</h2>

            {userRole === 'PROFISSIONAL' && (
                <div style={{ margin: '20px 0' }}>
                    <Link to="/servicos/novo">
                        <button style={{ padding: '10px', fontSize: '16px', cursor: 'pointer', backgroundColor: '#28a745', color: 'white', border: 'none', borderRadius: '5px' }}>
                            + Criar Novo Serviço
                        </button>
                    </Link>
                </div>
            )}

            {servicos.length === 0 ? (
                <p>Nenhum serviço cadastrado no momento.</p>
            ) : (
                <ul style={{ listStyle: 'none', padding: 0 }}>
                    {servicos.map(servico => (
                        <li key={servico.id} style={{ marginBottom: '10px', borderBottom: '1px solid #ccc', paddingBottom: '10px' }}>
                            <strong>{servico.nome}</strong> - R$ {servico.preco}
                            <br />
                            <small>Duração: {servico.duracaoMinutos} minutos</small>
                            
                            {/* --- ÁREA DE AÇÕES (EDITAR E EXCLUIR) --- */}
                            {userRole === 'PROFISSIONAL' && (
                                <div style={{ marginTop: '10px', display: 'flex', gap: '10px' }}>
                                    
                                    {/* 1. BOTÃO EDITAR (AZUL) */}
                                    {/* Note que passamos o ID na URL: /servicos/editar/123 */}
                                    <Link to={`/servicos/editar/${servico.id}`}>
                                        <button style={{ 
                                            background: '#007bff', // Azul
                                            color: 'white', 
                                            border: 'none', 
                                            padding: '5px 15px', 
                                            cursor: 'pointer', 
                                            borderRadius: '4px',
                                            fontSize: '14px'
                                        }}>
                                            Editar
                                        </button>
                                    </Link>

                                    {/* 2. BOTÃO EXCLUIR (VERMELHO) */}
                                    <button 
                                        onClick={() => handleDelete(servico.id)}
                                        style={{ 
                                            background: '#dc3545', 
                                            color: 'white', 
                                            border: 'none', 
                                            padding: '5px 15px', 
                                            cursor: 'pointer', 
                                            borderRadius: '4px',
                                            fontSize: '14px'
                                        }}
                                    >
                                        Excluir
                                    </button>
                                </div>
                            )}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default Servicos;