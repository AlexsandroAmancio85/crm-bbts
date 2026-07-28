package br.com.bbts.crm.importacao.dto;

/** Representa uma linha lida do XLS antes da persistência, já com eventual erro de validação. */
public record LinhaPlanilha(
        int numeroLinha,
        String nome,
        String propriedade,
        String cultura,
        String municipio,
        String telefone,
        String email,
        String erro
) {
    public boolean valida() {
        return erro == null;
    }
}
