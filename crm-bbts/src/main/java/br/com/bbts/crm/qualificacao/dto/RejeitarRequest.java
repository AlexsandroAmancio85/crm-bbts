package br.com.bbts.crm.qualificacao.dto;

import jakarta.validation.constraints.NotBlank;

public record RejeitarRequest(
        @NotBlank(message = "informe o motivo da rejeição") String motivo
) {}
