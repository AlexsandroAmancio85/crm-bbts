package br.com.bbts.crm.contato.dto;

import br.com.bbts.crm.contato.entity.ContatoCliente;

import java.time.LocalDateTime;

public record ContatoDTO(
        Long id, Long clienteId, Long vendedorId, String canal, String observacao, LocalDateTime dataContato
) {
    public static ContatoDTO from(ContatoCliente c) {
        return new ContatoDTO(
                c.getId(), c.getCliente().getId(),
                c.getVendedor() != null ? c.getVendedor().getId() : null,
                c.getCanal(), c.getObservacao(), c.getDataContato());
    }
}
