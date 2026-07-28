<div align="center">

# 🌾 CRM BBTS

### Plataforma de gestão comercial para o setor do agronegócio

Sistema completo de CRM voltado para produtores rurais, com foco em **crédito consignado**, modelando um funil de vendas de 8 etapas — da captação do lead ao fechamento.

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-Vite-61DAFB?logo=react)](https://react.dev/)
[![License](https://img.shields.io/badge/license-proprietary-red)](#-licença)

</div>

---

## 📋 Sobre o projeto

O **CRM BBTS** é uma aplicação full-stack construída para modernizar a gestão comercial de operações de crédito consignado voltadas a produtores rurais. O sistema acompanha o lead desde a qualificação inicial até o fechamento da venda, oferecendo visibilidade completa do pipeline para gerentes e vendedores.

### ✨ Principais funcionalidades

- 🔄 **Pipeline de vendas em 8 etapas**, adaptado à realidade do agronegócio
- 📥 **Importação de dados** via planilhas Excel (Apache POI)
- ✅ **Qualificação de leads assistida por IA** (integração com Gemini)
- 📅 **Agendamento** de visitas e follow-ups
- 📊 **Dashboard gerencial** com relatórios em tempo real
- 🔐 **Autenticação JWT** com controle de acesso por papel (gerente / vendedor)
- 🔗 **Stubs de integração** com Receita Federal e Dataprev

---

## 🏗️ Arquitetura

O projeto é dividido em dois módulos principais:

```
crm-bbts/
├── backend/     → Spring Boot 3 (Java 21) — modular monolith
└── frontend/    → React + Vite — rotas por papel (gerente / vendedor)
```

### Backend

| Módulo         | Responsabilidade                                   |
|----------------|-----------------------------------------------------|
| `import`       | Importação de planilhas Excel (Apache POI)          |
| `qualificacao` | Qualificação de leads com apoio de IA (Gemini)      |
| `vendas`       | Gestão do pipeline de vendas (8 etapas)             |
| `agendamento`  | Agendamento de visitas e contatos                   |
| `relatorios`   | Geração de relatórios                               |
| `dashboard`    | Visão consolidada para gestores                     |

### Frontend

- **React + Vite**, com roteamento condicional por papel do usuário (`gerente` × `vendedor`)
- Painel de assistente de IA integrado na tela de detalhe do cliente

---

## 🛠️ Stack tecnológica

**Backend**
- Java 21 + Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA + Flyway (migrations)
- Apache POI (leitura de planilhas)
- PostgreSQL (produção) / H2 (desenvolvimento)
- Integração com Google Gemini API

**Frontend**
- React + Vite
- Roteamento baseado em papel do usuário

**Infraestrutura**
- Backend + banco: [Render](https://render.com/)
- Frontend: [Vercel](https://vercel.com/)

---

## 🚀 Deploy

| Ambiente   | Link                                      |
|------------|--------------------------------------------|
| Backend    | 🔧 *em configuração — link será adicionado em breve* |
| Frontend   | 🔧 *em configuração — link será adicionado em breve* |

> Assim que os deploys estiverem estáveis, os links de produção serão atualizados aqui.

---

## ⚙️ Rodando localmente

### Pré-requisitos

- Java 21 (OpenJDK)
- Node.js 18+
- Maven (ou use o wrapper `./mvnw`)

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Variáveis de ambiente necessárias

Crie um arquivo `.env` (ou configure as variáveis do sistema) com:

```bash
GEMINI_API_KEY=sua_chave_aqui
```

> ⚠️ **Nunca** commite chaves de API diretamente no código ou nos arquivos `application.yml`. Use sempre variáveis de ambiente.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## 🔒 Segurança

- Autenticação stateless via **JWT**
- Chaves de API e segredos geridos exclusivamente por **variáveis de ambiente**
- Push Protection do GitHub habilitado no repositório

---

## 📄 Licença

Este projeto é **proprietário e de uso privado**. Todos os direitos reservados. É vedada a reprodução, distribuição ou uso não autorizado do código sem consentimento expresso do autor.

---

## 👤 Autor

**Alexsandro Amancio**
[LinkedIn](https://www.linkedin.com/in/alexsandroamancio1985/) · [GitHub](https://github.com/AlexsandroAmancio85)

</div>
