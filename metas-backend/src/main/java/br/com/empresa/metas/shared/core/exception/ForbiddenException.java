package br.com.empresa.metas.shared.core.exception;

/**
 * "Você não tem permissão pra fazer isso" — vira HTTP 403. Usada, por
 * exemplo, quando o usuário não tem permissão de Write para algum par
 * Centro de Custo + Cod Conta da planilha enviada.
 */
public class ForbiddenException extends AppException {
    public ForbiddenException(String code, String message) {
        super(code, message);
    }
}
