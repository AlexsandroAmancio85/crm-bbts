package br.com.bbts.crm.cliente.dto;

import br.com.bbts.crm.common.enums.StatusCliente;

/** Filtros opcionais aceitos por GET /api/clientes (query params). */
public record ClienteFiltroDTO(
        String busca,
        StatusCliente status,
        Long vendedorId,
        Boolean qualificado
) {}
