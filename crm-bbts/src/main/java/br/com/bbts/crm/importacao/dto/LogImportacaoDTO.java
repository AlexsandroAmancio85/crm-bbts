package br.com.bbts.crm.importacao.dto;

import br.com.bbts.crm.importacao.entity.LogImportacao;

public record LogImportacaoDTO(int linha, String mensagemErro) {
    public static LogImportacaoDTO from(LogImportacao l) {
        return new LogImportacaoDTO(l.getLinha(), l.getMensagemErro());
    }
}
