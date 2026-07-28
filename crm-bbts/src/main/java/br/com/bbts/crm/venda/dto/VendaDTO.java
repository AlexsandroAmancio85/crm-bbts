package br.com.bbts.crm.venda.dto;

import br.com.bbts.crm.venda.entity.Venda;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VendaDTO(
        Long id, Long clienteId, String clienteNome, Long vendedorId, String vendedorNome,
        BigDecimal valor, String observacao, LocalDateTime dataVenda
) {
    public static VendaDTO from(Venda v) {
        return new VendaDTO(
                v.getId(), v.getCliente().getId(), v.getCliente().getNome(),
                v.getVendedor().getId(), v.getVendedor().getNome(),
                v.getValor(), v.getObservacao(), v.getDataVenda());
    }
}
