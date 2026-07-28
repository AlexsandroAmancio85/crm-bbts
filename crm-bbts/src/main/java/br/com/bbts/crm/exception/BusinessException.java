package br.com.bbts.crm.exception;

/** Violação de regra de negócio (ex.: tentar distribuir um cliente não qualificado). */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
