package br.com.bbts.crm.agenda.dto;

import br.com.bbts.crm.agenda.entity.RetornoAgendado;

import java.time.LocalDateTime;

public record AgendaDTO(
        Long id, Long clienteId, String clienteNome, Long vendedorId,
        LocalDateTime dataAgendada, boolean concluido, String observacao
) {
    public static AgendaDTO from(RetornoAgendado r) {
        return new AgendaDTO(
                r.getId(), r.getCliente().getId(), r.getCliente().getNome(),
                r.getVendedor().getId(), r.getDataAgendada(), r.isConcluido(), r.getObservacao());
    }
}
