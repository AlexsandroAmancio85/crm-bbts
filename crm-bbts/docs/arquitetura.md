# Arquitetura — crm-bbts

## Visão geral

Monólito modular em Spring Boot 3 / Java 17, organizado por **domínio de negócio**
(cada etapa do fluxo vira um pacote com `controller/`, `service/`, `repository/`,
`entity/`, `dto/`), e não por camada técnica — facilita evoluir cada etapa de forma
independente e, futuramente, extrair um módulo para um serviço separado se necessário.

```
Front-end (React/Vite)  ─── REST/JSON + JWT ───►  crm-bbts (Spring Boot)
                                                          │
                                                          ▼
                                                   PostgreSQL / H2 (dev)
```

## Pacotes

| Pacote          | Etapa do fluxo                          |
|------------------|------------------------------------------|
| `importacao`     | 1–3 Recebimento, Importação, Validação    |
| `qualificacao`   | 4 Qualificação Gerencial                  |
| `vendedor`       | 5 Distribuição aos Vendedores             |
| `cliente`        | Entidade central, atravessa todas as etapas |
| `contato`, `agenda`, `venda` | 6 Atendimento do Vendedor (Contatado / Indisponível / Vendido) |
| `relatorio`, `dashboard` | 7–8 Monitoramento e Relatórios     |
| `usuario`, `security`, `config` | Autenticação/autorização (JWT) e infraestrutura transversal |

## Decisões técnicas

- **Flyway** controla o schema (`src/main/resources/db/migration`), nunca `ddl-auto: update`.
- **H2 em arquivo** (`./data/crmbbts`) no profile `dev` para rodar sem infraestrutura externa;
  **PostgreSQL** no profile `prod`.
- **JWT stateless** (`io.jsonwebtoken`), sem sessão de servidor — compatível com o front-end SPA.
- **Apache POI** faz a leitura das planilhas XLS/XLSX (`ExcelReaderService`).
- DTOs como `record` (imutáveis, sem boilerplate) em vez de expor entidades JPA diretamente.
