📅 AgendaTech (Sistema Fullstack)
O AgendaTech é uma solução completa para gestão de agendamentos de serviços, conectando prestadores de serviços a clientes de forma eficiente.

O projeto foi estruturado como um Monorepo, contendo tanto a API (Backend) quanto a Interface Web (Frontend).

🏛️ Arquitetura do Projeto
Este repositório está dividido em dois módulos principais:

Módulo	Tecnologia	Descrição
/backend
Java (Spring Boot)	API RESTful responsável pela lógica de negócios, segurança (JWT) e acesso a dados.
/frontend
React.js (Vite)	Interface moderna e responsiva para interação dos usuários com o sistema.

🚀 Tecnologias Utilizadas
•	Backend: Java 17, Spring Boot 3, Spring Security (JWT), Hibernate/JPA, MySQL (ou H2), Maven.
•	Frontend: React.js, Vite, Tailwind CSS, Axios.
•	Ferramentas: Git, VS Code, Insomnia/Postman.

⚙️ Como Rodar o Projeto
Para rodar a aplicação completa, você precisará iniciar os dois servidores (Back e Front) simultaneamente.

1. Rodar a API (Backend)
cd backend
./mvnw spring-boot:run
# O servidor iniciará em http://localhost:8081

2. Rodar a Interface (Frontend)
cd frontend
npm install
npm run dev
# O site iniciará em http://localhost:5173

🎯 Funcionalidades Principais
•	Cadastro e Autenticação de Usuários (Login seguro).
•	Gestão de Serviços (CRUD).
•	Agendamento de Horários.
•	Painel Administrativo.

