package br.com.bbts.crm.logprodutividade.dto;

import br.com.bbts.crm.logprodutividade.entity.LogProdutividade;

import java.time.LocalDateTime;

public record LogProdutividadeDTO(
        Long id, Long vendedorId, String vendedorNome,
        Long clienteId, String clienteNome,
        String tipoAtividade, String observacao, LocalDateTime dataHora
) {
    public static LogProdutividadeDTO from(LogProdutividade l) {
        return new LogProdutividadeDTO(
                l.getId(), l.getVendedor().getId(), l.getVendedor().getNome(),
                l.getCliente().getId(), l.getCliente().getNome(),
                l.getTipoAtividade(), l.getObservacao(), l.getDataHora());
    }
}
