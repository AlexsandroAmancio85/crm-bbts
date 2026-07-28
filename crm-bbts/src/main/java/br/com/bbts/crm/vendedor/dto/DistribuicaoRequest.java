package br.com.bbts.crm.vendedor.dto;

import jakarta.validation.constraints.NotNull;

public record DistribuicaoRequest(
        @NotNull(message = "informe o cliente") Long clienteId,
        @NotNull(message = "informe o vendedor") Long vendedorId
) {}
