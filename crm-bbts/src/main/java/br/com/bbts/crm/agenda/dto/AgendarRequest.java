package br.com.bbts.crm.agenda.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendarRequest(
        @NotNull(message = "informe o cliente") Long clienteId,
        @NotNull(message = "informe o vendedor") Long vendedorId,
        @NotNull(message = "informe a data do retorno") LocalDateTime dataAgendada,
        String observacao
) {}
