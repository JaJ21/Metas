package br.com.empresa.metas.shared.core.exception;

/**
 * "Não encontrei o que você pediu" — vira HTTP 404 (ver
 * GlobalExceptionHandler). Ex: buscar uma meta por um Centro de Custo +
 * Cod Conta que não existe.
 */
public class NotFoundException extends AppException {
    public NotFoundException(String code, String message) {
        super(code, message);
    }
}
