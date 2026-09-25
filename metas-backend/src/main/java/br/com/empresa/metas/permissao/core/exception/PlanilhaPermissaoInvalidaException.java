package br.com.empresa.metas.permissao.core.exception;

import br.com.empresa.metas.shared.core.exception.ValidationException;

public class PlanilhaPermissaoInvalidaException extends ValidationException {
    public PlanilhaPermissaoInvalidaException(String motivo) {
        super("PLANILHA_PERMISSAO_INVALIDA", motivo);
    }
}
