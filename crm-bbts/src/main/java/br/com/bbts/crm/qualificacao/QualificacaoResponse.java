package br.com.bbts.crm.qualificacao;

public record QualificacaoResponse(
    String prioridade,
    String justificativa,
    String proximoPassoRecomendado
) {}