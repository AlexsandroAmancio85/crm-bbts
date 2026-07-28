# Regras de negócio — crm-bbts

1. **Recebimento/Importação**: toda planilha enviada é lida linha a linha; linhas sem nome
   do produtor são marcadas com erro e não geram `Cliente`, mas ficam registradas em
   `LogImportacao` para o gerente revisar.
2. **Validação**: um arquivo importado só muda para `VALIDADO` quando o gerente confirma
   (`POST /importacoes/{id}/validar`); se ainda houver linhas com erro, o status final é `COM_ERRO`.
3. **Qualificação**: um cliente só pode ser distribuído a um vendedor se `qualificado = true`.
   Tentar distribuir um cliente não qualificado lança `BusinessException` (HTTP 422).
4. **Distribuição**: um cliente só pode ter um vendedor responsável por vez (campo único
   `vendedor_id` em `cliente`); reatribuir sobrescreve o vendedor anterior.
5. **Atendimento**: o status do cliente (`PENDENTE`, `CONTATADO`, `VENDIDO`, `INDISPONIVEL`)
   é atualizado pelo vendedor a cada contato. Registrar uma venda (`POST /vendas`) força o
   status do cliente para `VENDIDO` automaticamente.
6. **Monitoramento/Relatórios**: leitura agregada por vendedor e por mês; não altera dados,
   apenas consulta.
