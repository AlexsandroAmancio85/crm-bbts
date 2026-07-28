package br.com.bbts.crm.cliente.dto;

import br.com.bbts.crm.cliente.entity.Cliente;
import br.com.bbts.crm.common.enums.StatusCliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClienteDTO(
        Long id, String nome, String cpf, String propriedade, String cultura,
        String municipio, String telefone, String email,
        StatusCliente status, boolean qualificado,
        String convenio, Integer prazo, BigDecimal valorVendido, String observacao,
        Long vendedorId, String vendedorNome,
        LocalDateTime dataCriacao, LocalDateTime dataAtualizacao
) {
    public static ClienteDTO from(Cliente c) {
        return new ClienteDTO(
                c.getId(), c.getNome(), c.getCpf(), c.getPropriedade(), c.getCultura(),
                c.getMunicipio(), c.getTelefone(), c.getEmail(),
                c.getStatus(), c.isQualificado(),
                c.getConvenio(), c.getPrazo(), c.getValorVendido(), c.getObservacao(),
                c.getVendedor() != null ? c.getVendedor().getId() : null,
                c.getVendedor() != null ? c.getVendedor().getNome() : null,
                c.getDataCriacao(), c.getDataAtualizacao()
        );
    }
}
