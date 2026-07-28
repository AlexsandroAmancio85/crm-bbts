package br.com.bbts.crm.dashboard.dto;

import java.util.Map;

/** Espelha as 8 etapas do fluxograma, com a contagem de registros em cada uma. */
public record TrilhaDTO(Map<String, Long> contagens) {}
