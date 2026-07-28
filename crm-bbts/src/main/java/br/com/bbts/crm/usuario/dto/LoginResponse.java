package br.com.bbts.crm.usuario.dto;

public record LoginResponse(
        String token,
        String tipo,
        String username,
        String nome,
        String perfil,
        long expiraEm
) {}
