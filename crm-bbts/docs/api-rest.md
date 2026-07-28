# API REST — crm-bbts

Documentação interativa disponível em `/swagger-ui.html` (springdoc-openapi) assim que a
aplicação estiver no ar. Resumo dos principais endpoints, todos prefixados com `/api`:

| Método | Rota                              | Etapa | Descrição                              |
|--------|------------------------------------|-------|------------------------------------------|
| POST   | `/auth/login`                      | —     | Autentica e devolve um JWT               |
| GET    | `/clientes`                        | —     | Lista produtores (filtros: busca, status, vendedorId, qualificado) |
| GET/POST/PUT/DELETE | `/clientes/{id}`     | —     | CRUD de produtor                         |
| PATCH  | `/clientes/{id}/status`            | 6     | Atualiza status (Contatado/Vendido/Indisponível) |
| GET    | `/importacoes`                     | 1–3   | Lista arquivos importados                |
| POST   | `/importacoes/upload`              | 1–2   | Envia planilha XLS/XLSX                  |
| POST   | `/importacoes/{id}/validar`        | 3     | Confirma validação da base               |
| GET    | `/importacoes/{id}/log`            | 3     | Linhas com erro                          |
| GET    | `/qualificacoes/pendentes`         | 4     | Clientes aguardando qualificação         |
| POST   | `/qualificacoes/{clienteId}`       | 4     | Aprova qualificação                      |
| POST   | `/qualificacoes/{clienteId}/rejeitar` | 4  | Rejeita qualificação                     |
| GET    | `/vendedores`                      | 5     | Lista vendedores e tamanho da carteira   |
| GET    | `/vendedores/{id}/carteira`        | 5     | Clientes do vendedor                     |
| POST   | `/vendedores/distribuicao`         | 5     | Atribui cliente a vendedor               |
| GET    | `/contatos/cliente/{clienteId}`    | 6     | Histórico de contatos                    |
| POST   | `/contatos`                        | 6     | Registra um contato                      |
| GET/POST | `/agenda`                         | 6     | Retornos agendados                       |
| PATCH  | `/agenda/{id}/concluir`            | 6     | Marca retorno como concluído             |
| GET/POST | `/vendas`                         | 6     | Registra venda (status do cliente → VENDIDO) |
| GET    | `/relatorios/indicadores-mensais`  | 8     | Série mensal Vendido/Contatado/Indisponível |
| GET    | `/relatorios/exportar`             | 8     | Exporta CSV                              |
| GET    | `/dashboard/trilha`                | —     | Contagem por etapa (alimenta o componente "Trilha" do front) |
| GET    | `/dashboard/kpis`                  | —     | KPIs do topo do Dashboard                |

Autenticação: enviar `Authorization: Bearer <token>` obtido em `/auth/login`
(usuário seed: `admin` / `admin123`, ver `V011__seed_dados.sql` — trocar em produção).
