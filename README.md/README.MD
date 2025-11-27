📅 AgendaTech - Sistema de Agendamento Profissional

📖 Sobre o Projeto

O AgendaTech é uma solução completa (Fullstack) para gestão de serviços e agendamentos. O sistema conecta Profissionais prestadores de serviços a Clientes, permitindo o agendamento de horários de forma automatizada e inteligente.

O sistema conta com painéis distintos para cada tipo de utilizador, gestão financeira simplificada e controlo total sobre a agenda.

✨ Funcionalidades Principais

🔐 Autenticação e Segurança

Login e Registo de utilizadores (Clientes e Profissionais).

Autenticação via Tokens JWT (JSON Web Token).

Proteção de rotas no Frontend e Endpoints seguros no Backend.

🛠️ Gestão de Serviços (CRUD)

O Profissional pode Criar, Listar, Editar e Excluir os seus serviços.

Definição de preços e duração de cada atendimento.

📅 Motor de Agendamento Inteligente

Clientes podem visualizar serviços e agendar horários.

Validação de Conflitos: O sistema impede agendamentos duplicados no mesmo horário.

Histórico de agendamentos para Clientes e Profissionais.

✅ Gestão de Fluxo

O Profissional pode aceitar (CONFIRMADO) ou recusar (CANCELADO) agendamentos.

Finalização de serviços (CONCLUÍDO) para contabilização financeira.

📊 Dashboard Financeiro

Painel exclusivo para Profissionais.

Métricas em tempo real:

Faturação Total (Baseada em serviços concluídos).

Atendimentos Hoje.

Total de Clientes atendidos.

🚀 Tecnologias Utilizadas

Backend (API)

Java 17+

Spring Boot 3

Spring Security (Autenticação e Autorização)

Spring Data JPA (Persistência de dados)

MySQL (Base de dados relacional)

Lombok (Produtividade)

Frontend (Interface)

React.js (Biblioteca principal)

Vite (Build tool rápida)

Axios (Comunicação com API)

React Router Dom (Navegação)

CSS Moderno (Design responsivo e limpo)

📦 Como Rodar o Projeto Localmente

Pré-requisitos

Java JDK 17 ou superior.

Node.js e NPM.

MySQL instalado e rodando.

1. Configurar o Banco de Dados

Crie um banco de dados no MySQL chamado agendatech:

CREATE DATABASE agendatech;


2. Rodar o Backend

Entre na pasta do backend (cd agendatech).

Configure o arquivo application.properties com o seu usuário/senha do MySQL.

Execute o projeto:

./mvnw clean spring-boot:run


3. Rodar o Frontend

Entre na pasta do frontend (cd agendatech-web).

Instale as dependências e inicie:

npm install
npm run dev


Aceda a http://localhost:5173 no navegador.

🎨 Layout e Telas

O projeto possui um design moderno, limpo e responsivo, focado na experiência do utilizador (UX).

Dashboard: Visualização clara de métricas.

Minha Agenda: Tabela interativa com ações rápidas e indicadores de status coloridos.

Formulários: Inputs estilizados e validações visuais.

👨‍💻 Autor

Desenvolvido por [Seu Nome Aqui].
Entre em contacto!