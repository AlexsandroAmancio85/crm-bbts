package br.com.bbts.crm.usuario.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "informe o usuário") String username,
        @NotBlank(message = "informe a senha") String senha
) {}
