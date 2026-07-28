// Dados de demonstração — refletem as entidades do back-end (Cliente, Vendedor,
// QualificacaoCliente, Oportunidade, Venda). Substituídos pelas chamadas reais
// assim que os endpoints REST do crm-bbts estiverem disponíveis (ver src/api/*).

export const vendedores = [
  { id: 1, nome: 'Marcos Teixeira', regiao: 'Sul do Ceará', carteiraAtual: 18 },
  { id: 2, nome: 'Janaína Souza', regiao: 'Sertão Central', carteiraAtual: 22 },
  { id: 3, nome: 'Roberto Lima', regiao: 'Litoral Oeste', carteiraAtual: 15 },
]

export const estagiosTrilha = [
  { no: 1, chave: 'recebimento', label: 'Recebimento da Base' },
  { no: 2, chave: 'importacao', label: 'Importação e Leitura' },
  { no: 3, chave: 'validacao', label: 'Validação da Base' },
  { no: 4, chave: 'qualificacao', label: 'Qualificação Gerencial' },
  { no: 5, chave: 'distribuicao', label: 'Distribuição aos Vendedores' },
  { no: 6, chave: 'atendimento', label: 'Atendimento do Vendedor' },
  { no: 7, chave: 'monitoramento', label: 'Monitoramento Gerencial' },
  { no: 8, chave: 'relatorios', label: 'Relatórios e Indicadores' },
]

export const clientes = [
  { id: 101, nome: 'Fazenda Boa Vista', propriedade: 'Sítio Boa Vista', cultura: 'Milho', municipio: 'Quixadá', telefone: '(88) 99811-2233', status: 'contatado', vendedorId: 1, qualificado: true },
  { id: 102, nome: 'Agropecuária Serra Verde', propriedade: 'Fazenda Serra Verde', cultura: 'Soja', municipio: 'Iguatu', telefone: '(88) 99744-5566', status: 'vendido', vendedorId: 1, qualificado: true },
  { id: 103, nome: 'José Carlos Pereira', propriedade: 'Sítio Riacho Fundo', cultura: 'Feijão', municipio: 'Tauá', telefone: '(88) 99677-8899', status: 'indisponivel', vendedorId: 2, qualificado: true },
  { id: 104, nome: 'Fazenda Três Irmãos', propriedade: 'Fazenda Três Irmãos', cultura: 'Algodão', municipio: 'Crateús', telefone: '(88) 99622-3344', status: 'pendente', vendedorId: null, qualificado: false },
  { id: 105, nome: 'Cooperativa Vale Fértil', propriedade: '—', cultura: 'Milho / Soja', municipio: 'Sobral', telefone: '(88) 99511-7788', status: 'contatado', vendedorId: 3, qualificado: true },
  { id: 106, nome: 'Antônia Ferreira Lima', propriedade: 'Chácara Esperança', cultura: 'Mandioca', municipio: 'Russas', telefone: '(88) 99455-1122', status: 'vendido', vendedorId: 2, qualificado: true },
  { id: 107, nome: 'Fazenda Pedra Branca', propriedade: 'Fazenda Pedra Branca', cultura: 'Soja', municipio: 'Limoeiro do Norte', telefone: '(88) 99388-9911', status: 'pendente', vendedorId: null, qualificado: false },
  
  // 🔴 NOVO: Exemplo para simular PRIORIDADE ALTA no Gemini
  { 
    id: 108, 
    nome: 'Agronegócios Ceará S.A.', 
    propriedade: 'Fazenda Horizonte', 
    cultura: 'Soja e Milho', 
    municipio: 'Aquiraz', 
    telefone: '(85) 99122-3344', 
    status: 'pendente', 
    vendedorId: null, 
    qualificado: false,
    faturamentoEstimado: 8500000.00,
    quantidadeFuncionarios: 120,
    segmento: 'Agronegócio de Grande Porte',
    observacoes: 'O produtor demonstrou altíssima urgência na implementação de sistemas automatizados de monitoramento de safra e quer fechar um contrato corporativo de suporte técnico ainda esta semana.'
  },

  // 🟡 NOVO: Exemplo para simular PRIORIDADE MÉDIA no Gemini
  { 
    id: 109, 
    nome: 'Carlos Eduardo Alencar', 
    propriedade: 'Sítio Primavera', 
    cultura: 'Melão e Melancia', 
    municipio: 'Caucaia', 
    telefone: '(85) 99233-4455', 
    status: 'pendente', 
    vendedorId: null, 
    qualificado: false,
    faturamentoEstimado: 1200000.00,
    quantidadeFuncionarios: 25,
    segmento: 'Médio Produtor Rural',
    observacoes: 'Produtor interessado em modernizar o sistema de irrigação automatizada. Possui capital para investimento, mas a tomada de decisão depende de uma assembleia familiar que ocorrerá no próximo mês.'
  },

  // 🟢 NOVO: Exemplo para simular PRIORIDADE BAIXA no Gemini
  { 
    id: 110, 
    nome: 'Maria das Dores Silva', 
    propriedade: 'Chácara Bela Vista', 
    cultura: 'Hortaliças Orgânicas', 
    municipio: 'Fortaleza', 
    telefone: '(85) 99344-5566', 
    status: 'pendente', 
    vendedorId: null, 
    qualificado: false,
    faturamentoEstimado: 45000.00,
    quantidadeFuncionarios: 2,
    segmento: 'Agricultura Familiar',
    observacoes: 'Entrou em contato apenas para tirar dúvidas sobre linhas de crédito e solicitar um orçamento informativo sem compromisso. Não possui previsão de investimentos em infraestrutura para este ano.'
  }
]

export const importacoes = [
  { id: 1, arquivo: 'base_produtores_jun_2026.xlsx', dataImportacao: '2026-06-18T09:12:00', linhasLidas: 320, linhasValidas: 298, linhasComErro: 22, status: 'VALIDADO' },
  { id: 2, arquivo: 'base_produtores_mai_2026.xlsx', dataImportacao: '2026-05-21T14:30:00', linhasLidas: 410, linhasValidas: 401, linhasComErro: 9, status: 'VALIDADO' },
  { id: 3, arquivo: 'leads_feira_agro_ce.xlsx', dataImportacao: '2026-06-20T11:05:00', linhasLidas: 145, linhasValidas: 0, linhasComErro: 0, status: 'AGUARDANDO_VALIDACAO' },
]

export const indicadoresMensais = [
  { mes: 'Jan', vendidos: 12, contatados: 40, indisponiveis: 8 },
  { mes: 'Fev', vendidos: 18, contatados: 38, indisponiveis: 11 },
  { mes: 'Mar', vendidos: 15, contatados: 44, indisponiveis: 9 },
  { mes: 'Abr', vendidos: 22, contatados: 51, indisponiveis: 13 },
  { mes: 'Mai', vendidos: 27, contatados: 49, indisponiveis: 10 },
  { mes: 'Jun', vendidos: 19, contatados: 35, indisponiveis: 7 },
]

export const statusMeta = {
  vendido: { label: 'Vendido', className: 'vendido' },
  indisponivel: { label: 'Indisponível', className: 'indisponivel' },
  contatado: { label: 'Contatado', className: 'contatado' },
  pendente: { label: 'Pendente', className: 'pendente' },
}
