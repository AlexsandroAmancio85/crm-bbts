package br.com.bbts.crm.logsistema.dto;

import br.com.bbts.crm.logsistema.entity.LogSistema;

import java.time.LocalDateTime;

public record LogSistemaDTO(
        Long id, Long usuarioId, String usuarioNome,
        String acao, String entidade, Long entidadeId,
        String detalhes, String ip, LocalDateTime dataHora
) {
    public static LogSistemaDTO from(LogSistema l) {
        return new LogSistemaDTO(
                l.getId(), l.getUsuario().getId(), l.getUsuario().getNome(),
                l.getAcao(), l.getEntidade(), l.getEntidadeId(),
                l.getDetalhes(), l.getIp(), l.getDataHora());
    }
}
