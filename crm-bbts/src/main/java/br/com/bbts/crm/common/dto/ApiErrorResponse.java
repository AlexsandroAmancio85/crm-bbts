package br.com.bbts.crm.common.dto;

import java.time.LocalDateTime;
import java.util.List;

/** Corpo padronizado de erro devolvido pelo GlobalExceptionHandler. */
public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> detalhes
) {
    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(LocalDateTime.now(), status, error, message, path, List.of());
    }

    public static ApiErrorResponse of(int status, String error, String message, String path, List<String> detalhes) {
        return new ApiErrorResponse(LocalDateTime.now(), status, error, message, path, detalhes);
    }
}
