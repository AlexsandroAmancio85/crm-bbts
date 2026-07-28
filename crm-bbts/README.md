# crm-bbts — Backend (Java / Spring Boot)

API REST do CRM da BBTS para o agronegócio — implementa as 8 etapas do fluxo (Recebimento,
Importação, Validação, Qualificação Gerencial, Distribuição, Atendimento do Vendedor,
Monitoramento, Relatórios), integrada ao front-end `crm-bbts-frontend` (React/Vite) já entregue.

## Stack

- **Java 21** + **Spring Boot 3.3**
- **Spring Web**, **Spring Data JPA**, **Spring Security** (JWT via `io.jsonwebtoken`)
- **Flyway** (migrações versionadas) + **H2** (dev, arquivo local) / **PostgreSQL** (prod)
- **Apache POI** — leitura de planilhas XLS/XLSX
- **springdoc-openapi** — Swagger UI em `/swagger-ui.html`
- **Lombok**, **Maven**

## Como rodar

Pré-requisitos: **JDK 21+** e **Maven** (ou use o wrapper, se preferir adicionar um).

```bash
mvn spring-boot:run
# ou, depois de compilar:
mvn clean package
java -jar target/crm-bbts.jar
```

A aplicação sobe em `http://localhost:8080` com o profile `dev` (padrão), usando um banco
H2 em arquivo (`./data/crmbbts.mv.db`) — não precisa instalar nada além do JDK para testar.
O Flyway roda as migrações e o seed automaticamente na primeira subida (usuário `admin` /
senha `admin123`, três vendedores e cinco produtores de exemplo).

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Console H2 (apenas dev): http://localhost:8080/h2-console`` — JDBC URL `jdbc:h2:file:./data/crmbbts`

Para produção, ative o profile `prod` (`-Dspring.profiles.active=prod`) e configure
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` e `CORS_ALLOWED_ORIGINS` via variáveis
de ambiente (ver `application-prod.yml`).

## Como abrir e rodar no IntelliJ IDEA

1. **File → Open…** e selecione a pasta `crm-bbts` (a que contém `pom.xml`).
2. O IntelliJ detecta o Maven automaticamente e baixa as dependências (ícone do Maven na
   lateral direita → *Reload All Maven Projects* se precisar forçar).
3. Confirme o **Project SDK** em **File → Project Structure → Project** como Java 17+.
4. Habilite o **annotation processing** do Lombok: **Settings → Build, Execution, Deployment
   → Compiler → Annotation Processors** → marque *Enable annotation processing* (o plugin
   Lombok já vem instalado por padrão no IntelliJ Ultimate/Community recentes; instale-o em
   **Settings → Plugins** caso não apareça).
5. Rode a classe `CrmBbtsApplication` (botão ▶ ao lado do `main`) ou crie uma *Run
   Configuration* do tipo **Maven** com o goal `spring-boot:run`.
6. Para rodar junto com o front-end: abra os dois projetos em duas janelas do IntelliJ
   (ou como dois módulos do mesmo workspace), suba o back-end primeiro (porta 8080) e depois
   `npm run dev` no front (porta 5173, com proxy de `/api` já configurado).

## Integração com o front-end (`crm-bbts-frontend`)

Os endpoints abaixo já são exatamente os que `src/api/*.js` do front-end espera — não é
necessário alterar nada no React, só ligar os serviços que hoje usam `mock/data.js`:

- `POST /api/auth/login` → devolve `{ token, tipo, username, nome, perfil, expiraEm }`.
  Salve o `token` em `localStorage['crm-bbts:token']` (é exatamente a chave que
  `src/api/axiosConfig.js` já lê no front).
- `GET/POST/PUT/DELETE /api/clientes`, `PATCH /api/clientes/{id}/status`
- `GET /api/importacoes`, `POST /api/importacoes/upload` (multipart), `POST /api/importacoes/{id}/validar`, `GET /api/importacoes/{id}/log`
- `GET /api/qualificacoes/pendentes`, `POST /api/qualificacoes/{clienteId}`, `POST /api/qualificacoes/{clienteId}/rejeitar`
- `GET /api/vendedores`, `GET /api/vendedores/{id}/carteira`, `POST /api/vendedores/distribuicao`
- `GET /api/contatos/cliente/{clienteId}`, `POST /api/contatos`
- `GET/POST /api/agenda`, `PATCH /api/agenda/{id}/concluir`
- `GET/POST /api/vendas`
- `GET /api/relatorios/indicadores-mensais`, `GET /api/relatorios/exportar`
- `GET /api/dashboard/trilha`, `GET /api/dashboard/kpis`

Detalhamento completo em `docs/api-rest.md`. CORS já libera `http://localhost:5173` por
padrão (`crm.cors.allowed-origins` em `application.yml`).

### Plugando uma página do front no back-end real

Cada página do front tem comentários `// Integração real: ...` indicando onde trocar o mock
pela chamada ao serviço. Exemplo (`QualificacaoPage.jsx`):

```js
// antes (mock):
setClientes((prev) => prev.map(c => c.id === id ? { ...c, qualificado: true } : c))

// depois (back-end real):
import { qualificacaoService } from '../../api/qualificacaoService'
useEffect(() => {
  qualificacaoService.listarPendentes().then(res => setPendentes(res.data))
}, [])
qualificacaoService.qualificar(id, {})
```

## Estrutura

Segue exatamente o layout combinado: pacotes por domínio (`cliente`, `importacao`,
`qualificacao`, `vendedor`, `oportunidade`, `contato`, `agenda`, `venda`, `relatorio`,
`dashboard`), cada um com `controller/`, `service/`, `repository/`, `entity/`, `dto/`, mais
os pacotes transversais `config/`, `security/`, `exception/`, `common/` e o módulo `usuario/`
(login/autenticação). Veja `docs/arquitetura.md` para detalhes.

## Limitação do ambiente em que este projeto foi gerado

Este projeto foi escrito e revisado estaticamente (verificação de pacotes, chaves e
balanceamento de código), mas **não foi compilado com Maven aqui**, pois o ambiente de
geração não tem acesso ao Maven Central. Rode `mvn clean package` (ou o build do IntelliJ)
na primeira vez que abrir o projeto para confirmar que tudo baixa e compila — qualquer erro
de dependência/versão é rápido de ajustar a partir do log do Maven.

## Próximos passos sugeridos

- Trocar a query simplificada de `RelatorioService.indicadoresMensais()` por um
  `@Query` nativo agrupando por mês/ano real (a versão atual distribui o total
  proporcionalmente, só para o dashboard não ficar vazio no MVP).
- Paginação server-side em `GET /clientes` quando a base crescer.
- Testes de integração por módulo (`@DataJpaTest`, `@WebMvcTest`).
- Tela de cadastro/gestão de usuários (hoje só existe o seed `admin`).
