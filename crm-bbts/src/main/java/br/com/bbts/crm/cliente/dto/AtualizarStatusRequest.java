package br.com.bbts.crm.cliente.dto;

import br.com.bbts.crm.common.enums.StatusCliente;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(
        @NotNull(message = "informe o novo status") StatusCliente status
) {}
