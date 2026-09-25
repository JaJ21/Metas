package br.com.empresa.metas.shared.core.exception;

/**
 * "Os dados que você mandou não são válidos" — vira HTTP 422 (ver
 * GlobalExceptionHandler). Equivalente ao ValidacaoError do sistema em
 * Python: usada quando a planilha/linhas enviadas têm letra onde não
 * podia, colunas faltando, linhas duplicadas, etc.
 */
public class ValidationException extends AppException {
    public ValidationException(String code, String message) {
        super(code, message);
    }
}
