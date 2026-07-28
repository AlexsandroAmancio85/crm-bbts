package br.com.bbts.crm.usuario.dto;

import br.com.bbts.crm.common.enums.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarUsuarioRequest(
        @NotBlank String nome,
        @NotBlank String username,
        @NotBlank String senha,
        @NotNull PerfilUsuario perfil,
        Long vendedorId
) {}
