import React from 'react'
import ReactDOM from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

// 1. Importações das Páginas
import App from './App.jsx'
import Register from './pages/Register.jsx';
import Login from './pages/Login.jsx';
import Dashboard from './pages/Dashboard.jsx'; // <--- IMPORTADO AQUI
import Servicos from './pages/Servicos.jsx';
import CriarServico from './pages/CriarServico.jsx'; 
import AgendarServico from './pages/AgendarServico.jsx';
import MeusAgendamentos from './pages/MeusAgendamentos.jsx';

const router = createBrowserRouter([
  {
    path: "/", 
    element: <App />, 
    children: [
      {
        path: "/login",
        element: <Login />
      },
      {
        path: "/register",
        element: <Register />
      },
      // --- ROTA DO DASHBOARD ---
      {
        path: "/dashboard",
        element: <Dashboard />
      },
      // -------------------------
      {
        path: "/servicos",
        element: <Servicos />
      },
      {
        path: "/servicos/novo",
        element: <CriarServico />
      },
      {
        path: "/servicos/editar/:id", 
        element: <CriarServico /> 
      },
      {
        path: "/agendar",
        element: <AgendarServico />
      },
      {
        path: "/minha-agenda",
        element: <MeusAgendamentos />
      }
    ]
  }
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
)