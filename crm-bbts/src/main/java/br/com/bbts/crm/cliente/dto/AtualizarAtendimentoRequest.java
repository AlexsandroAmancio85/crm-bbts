package br.com.bbts.crm.cliente.dto;

import br.com.bbts.crm.common.enums.StatusCliente;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Atualização de atendimento pelo vendedor (item 4 do fluxo).
 * Abrange os três desfechos: VENDIDO (4.1), INDISPONIVEL (4.2), CONTATADO (4.3).
 */
public record AtualizarAtendimentoRequest(
        @NotNull(message = "informe o status") StatusCliente status,
        // VENDIDO (4.1)
        String convenio,
        Integer prazo,
        BigDecimal valorVendido,
        // INDISPONIVEL / CONTATADO (4.2 / 4.3)
        String observacao
) {}
