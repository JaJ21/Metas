package br.com.empresa.metas.shared.core.exception;

/**
 * "Suas credenciais não são válidas" — vira HTTP 401. Usada no login
 * (usuário/senha errados) e quando o JWT enviado é inválido/expirado.
 */
public class UnauthorizedException extends AppException {
    public UnauthorizedException(String code, String message) {
        super(code, message);
    }
}
