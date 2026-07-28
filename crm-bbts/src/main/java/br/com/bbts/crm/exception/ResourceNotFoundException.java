package br.com.bbts.crm.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String entidade, Object id) {
        return new ResourceNotFoundException(entidade + " não encontrado(a) com id " + id);
    }
}
