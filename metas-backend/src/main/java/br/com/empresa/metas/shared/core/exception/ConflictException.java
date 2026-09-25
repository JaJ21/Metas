package br.com.empresa.metas.shared.core.exception;

/**
 * "Isso conflita com algo que já existe" — vira HTTP 409. Ex: tentar
 * cadastrar duas permissões idênticas, ou um CPF duplicado onde não pode.
 */
public class ConflictException extends AppException {
    public ConflictException(String code, String message) {
        super(code, message);
    }
}
