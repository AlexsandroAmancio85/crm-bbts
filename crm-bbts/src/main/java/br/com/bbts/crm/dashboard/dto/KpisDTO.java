package br.com.bbts.crm.dashboard.dto;

public record KpisDTO(
        long produtoresNaBase,
        long qualificados,
        long vendidosNoMes,
        double taxaConversaoPercentual
) {}
