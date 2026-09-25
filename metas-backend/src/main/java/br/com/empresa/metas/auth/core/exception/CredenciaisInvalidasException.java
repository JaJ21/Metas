package br.com.empresa.metas.auth.core.exception;

import br.com.empresa.metas.shared.core.exception.UnauthorizedException;

public class CredenciaisInvalidasException extends UnauthorizedException {
    public CredenciaisInvalidasException() {
        super("CREDENCIAIS_INVALIDAS", "CPF ou senha inválidos.");
    }
}
