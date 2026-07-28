# CRM BBTS — Frontend (Agronegócio)

Frontend React (Vite) do MVP do CRM da BBTS para o setor do agronegócio. Implementa, tela a
tela, o fluxo definido no fluxograma do projeto:

```
1. Recebimento da Base   → Upload da planilha XLS
2. Importação e Leitura  → Leitura/parse via ExcelReaderService (back-end)
3. Validação da Base     → Linhas válidas vs. com erro
4. Qualificação Gerencial→ Aprovar/rejeitar produtores importados
5. Distribuição          → Atribuir produtores qualificados a vendedores
6. Atendimento Vendedor  → Quadro com Vendido / Indisponível / Contatado
7. Monitoramento         → Gerência acompanha desempenho por vendedor
8. Relatórios            → Indicadores mensais + exportação
```

O elemento de navegação lateral ("Trilha da Safra") reproduz a mesma sequência das 8 etapas
do fluxograma, e cada página tem o respectivo número de etapa em destaque.

## Stack

- **React 18** + **Vite 5**
- **react-router-dom** — roteamento entre as etapas
- **axios** — chamadas REST ao back-end Java (`crm-bbts`)
- **recharts** — gráficos do dashboard e relatórios
- **lucide-react** — ícones

Não há Tailwind/UI-kit: o visual (paleta "campo/colheita", tipografia Fraunces + Work Sans)
está em `src/styles/tokens.css` e `src/styles/global.css`, pensado especificamente para o
domínio do agronegócio.

## Estrutura

```
src/
├── api/                  # 1 arquivo por módulo do back-end (cliente, importacao,
│                          # qualificacao, vendedor, contato, agenda, venda, relatorio, dashboard)
├── components/
│   ├── layout/            Sidebar.jsx, Topbar.jsx
│   └── common/             Trilha.jsx, DataTable.jsx, StatusBadge.jsx, Kpi.jsx
├── mock/data.js           Dados de demonstração (trocar pelas chamadas reais)
├── pages/                  Uma pasta por etapa do fluxo
├── App.jsx                 Rotas
└── main.jsx                 Bootstrap
```

Cada arquivo em `src/api/` já está nomeado e comentado para bater com os pacotes Java do
back-end (`br.com.bbts.crm.cliente`, `...importacao`, `...qualificacao`, `...vendedor`,
`...oportunidade`, `...contato`, `...agenda`, `...venda`, `...relatorio`, `...dashboard`),
bastando apontar as rotas para os endpoints reais quando os controllers estiverem prontos.
Hoje as páginas usam `src/mock/data.js` para já nascerem navegáveis sem o back-end no ar.

## Como rodar

Pré-requisito: **Node.js 18+** instalado.

```bash
npm install
npm run dev      # http://localhost:5173
```

Em desenvolvimento, o Vite já faz proxy de `/api/*` para `http://localhost:8080` (onde o
Spring Boot do `crm-bbts` deve estar rodando) — configurado em `vite.config.js`.

Para build de produção:

```bash
npm run build     # gera /dist
npm run preview   # serve o build localmente
```

## Como abrir e desenvolver no IntelliJ IDEA

1. Descompacte este projeto numa pasta, por exemplo ao lado do projeto back-end `crm-bbts/`.
2. No IntelliJ: **File → Open…** e selecione a pasta `crm-bbts-frontend`.
3. Instale o plugin **Node.js** (Settings → Plugins) se ainda não estiver ativo — o IntelliJ
   Ultimate já reconhece `package.json` e oferece *Run Configurations* para `npm run dev`.
4. Configure o interpretador Node em **Settings → Languages & Frameworks → Node.js**.
5. Crie uma *Run/Debug Configuration* do tipo **npm**, script `dev`, para subir o servidor
   com um clique (ou use o terminal integrado do IntelliJ com os comandos acima).
6. Para depurar junto com o back-end, abra os dois projetos como módulos de um mesmo
   workspace, ou em duas janelas do IntelliJ lado a lado — o front consome a API via proxy,
   então basta o back-end (`crm-bbts`) estar rodando na porta 8080.

## Ligar aos endpoints reais do back-end

Cada serviço em `src/api/*.js` já assume os caminhos REST esperados (ex.:
`GET /api/clientes`, `POST /api/importacoes/upload`, `POST /api/qualificacoes/{id}`,
`POST /api/vendedores/distribuicao`, `PATCH /api/clientes/{id}/status`,
`GET /api/relatorios/indicadores-mensais`). Quando os `Controller` correspondentes do
back-end estiverem implementados (`ClienteController`, `ImportacaoController`, etc.), basta:

1. Importar o serviço na página (ex. `import { clienteService } from '../../api/clienteService'`).
2. Substituir as chamadas a `mock/data.js` por `useEffect` + chamada ao serviço.
3. Remover o import de `mock/data.js` quando não for mais necessário.

As páginas já têm os pontos de integração comentados (`// Integração real: ...`) indicando
exatamente onde plugar cada chamada.

## Autenticação (JWT)

`src/api/axiosConfig.js` injeta o header `Authorization: Bearer <token>` lendo de
`localStorage['crm-bbts:token']`. A tela `/login` (`src/pages/Login/LoginPage.jsx`) já chama
`POST /api/auth/login` via `src/api/authService.js`, salva o token retornado e redireciona
para o dashboard — compatível com o `JwtService`/`JwtFilter`/`AuthController` do back-end
`crm-bbts`. Usuário seed do back-end: `admin` / `admin123`.

Por ora o guard de rota (`ProtectedLayout` em `App.jsx`) é otimista, para o front continuar
navegável com os dados mock mesmo sem o back-end no ar. Quando a integração estiver ativa,
basta trocá-lo para checar `authService.autenticado()` antes de liberar as rotas.

## Próximos passos sugeridos

- Tela de login integrada ao `SecurityConfig`/JWT do back-end.
- Paginação e filtros server-side em `ClientesPage` quando a base crescer.
- Testes (Vitest + Testing Library) para os componentes de `common/`.
- Code-splitting por rota (`React.lazy`) — o bundle atual já alerta sobre tamanho.
