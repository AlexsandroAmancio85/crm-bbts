package br.com.bbts.crm.common.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/** Envelope simples de paginação devolvido pelos endpoints de listagem. */
public record PageResponse<T>(
        List<T> conteudo,
        int paginaAtual,
        int totalPaginas,
        long totalRegistros
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getTotalPages(), page.getTotalElements());
    }
}
