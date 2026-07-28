package br.com.bbts.crm.usuario.dto;

import br.com.bbts.crm.common.enums.PerfilUsuario;
import br.com.bbts.crm.usuario.entity.Usuario;

public record UsuarioDTO(
        Long id, String nome, String username,
        PerfilUsuario perfil, boolean ativo, Long vendedorId, String vendedorNome
) {
    public static UsuarioDTO from(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNome(), u.getUsername(), u.getPerfil(), u.isAtivo(),
                u.getVendedor() != null ? u.getVendedor().getId() : null,
                u.getVendedor() != null ? u.getVendedor().getNome() : null);
    }
}
