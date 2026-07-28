package br.com.bbts.crm.qualificacao;

public record LeadDto(
    String nome,
    String empresa,
    Double faturamentoEstimado,
    Integer quantidadeFuncionarios,
    String segmento,
    String historicoContato
) {}
