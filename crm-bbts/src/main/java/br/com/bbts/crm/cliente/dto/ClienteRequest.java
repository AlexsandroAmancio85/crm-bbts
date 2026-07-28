package br.com.bbts.crm.cliente.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record ClienteRequest(
        @NotBlank(message = "informe o nome do produtor") String nome,
        String cpf,
        String propriedade,
        String cultura,
        String municipio,
        String telefone,
        String email
) {}
