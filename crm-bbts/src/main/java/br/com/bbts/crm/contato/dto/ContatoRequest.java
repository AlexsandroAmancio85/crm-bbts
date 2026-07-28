package br.com.bbts.crm.contato.dto;

import jakarta.validation.constraints.NotNull;

public record ContatoRequest(
        @NotNull(message = "informe o cliente") Long clienteId,
        Long vendedorId,
        String canal,
        String observacao
) {}
