package br.com.bbts.crm.venda.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VendaRequest(
        @NotNull(message = "informe o cliente") Long clienteId,
        @NotNull(message = "informe o vendedor") Long vendedorId,
        @NotNull(message = "informe o valor da venda")
        @DecimalMin(value = "0.0", inclusive = false, message = "o valor deve ser maior que zero")
        BigDecimal valor,
        String observacao
) {}
