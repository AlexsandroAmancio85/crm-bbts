package br.com.bbts.crm.oportunidade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OportunidadeRequest(
        @NotNull(message = "informe o cliente") Long clienteId,
        Long vendedorId,
        @NotBlank(message = "informe a descrição") String descricao,
        BigDecimal valorEstimado
) {}
