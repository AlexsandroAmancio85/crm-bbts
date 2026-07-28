package br.com.bbts.crm.importacao.dto;

import br.com.bbts.crm.common.enums.StatusImportacao;
import br.com.bbts.crm.importacao.entity.ArquivoImportado;

import java.time.LocalDateTime;

public record ImportacaoDTO(
        Long id,
        String arquivo,
        LocalDateTime dataImportacao,
        int linhasLidas,
        int linhasValidas,
        int linhasComErro,
        StatusImportacao status
) {
    public static ImportacaoDTO from(ArquivoImportado a) {
        return new ImportacaoDTO(
                a.getId(), a.getNomeArquivo(), a.getDataImportacao(),
                a.getLinhasLidas(), a.getLinhasValidas(), a.getLinhasComErro(), a.getStatus());
    }
}
