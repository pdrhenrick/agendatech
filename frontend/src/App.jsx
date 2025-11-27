import React, { useState, useEffect } from 'react';
import { Outlet, Link, useNavigate, useLocation } from 'react-router-dom';
import './App.css'; // Importa o CSS novo

function App() {
  const navigate = useNavigate();
  const location = useLocation(); 
  
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState(null); 

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    const role = localStorage.getItem('userRole'); 

    setIsLoggedIn(!!token); 
    setUserRole(role); 
  }, [location]);

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userRole');
    setIsLoggedIn(false);
    setUserRole(null);
    alert("Logout realizado com sucesso!");
    navigate('/login');
  };

  return (
    <div className="app-container">
      {/* Menu Superior */}
      <header className="navbar">
        <h1>Projeto AgendaTech</h1>
        
        <nav className="nav-links">
          {isLoggedIn ? (
            <>
              {userRole === 'PROFISSIONAL' && (
                <Link to="/dashboard" className="nav-link" style={{ color: '#f59e0b' }}>
                   📊 Dashboard
                </Link>
              )}

              <Link to="/agendar" className="nav-link">📅 Agendar</Link>
              <Link to="/minha-agenda" className="nav-link">Minha Agenda</Link>
              <Link to="/servicos" className="nav-link">Serviços</Link>
              
              {/* Botão Sair Redondinho */}
              <button className="btn-logout" onClick={handleLogout}>
                Sair
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="nav-link">Login</Link>
              <Link to="/register" className="nav-link">Registrar</Link>
            </>
          )}
        </nav>
      </header>
      
      {/* Conteúdo da Página */}
      <main className="main-content">
        <Outlet />
      </main>
    </div>
  )
}

export default App;