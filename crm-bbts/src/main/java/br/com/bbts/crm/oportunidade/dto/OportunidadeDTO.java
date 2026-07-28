package br.com.bbts.crm.oportunidade.dto;

import br.com.bbts.crm.common.enums.StatusOportunidade;
import br.com.bbts.crm.oportunidade.entity.Oportunidade;

import java.math.BigDecimal;

public record OportunidadeDTO(
        Long id, Long clienteId, Long vendedorId, String descricao,
        BigDecimal valorEstimado, StatusOportunidade status
) {
    public static OportunidadeDTO from(Oportunidade o) {
        return new OportunidadeDTO(
                o.getId(), o.getCliente().getId(),
                o.getVendedor() != null ? o.getVendedor().getId() : null,
                o.getDescricao(), o.getValorEstimado(), o.getStatus());
    }
}
