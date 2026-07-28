package br.com.bbts.crm.vendedor.dto;

import br.com.bbts.crm.vendedor.entity.Vendedor;

public record VendedorDTO(
        Long id,
        String nome,
        String email,
        String regiao,
        boolean ativo,
        long carteiraAtual
) {
    public static VendedorDTO from(Vendedor v, long carteiraAtual) {
        return new VendedorDTO(v.getId(), v.getNome(), v.getEmail(), v.getRegiao(), v.isAtivo(), carteiraAtual);
    }
}
