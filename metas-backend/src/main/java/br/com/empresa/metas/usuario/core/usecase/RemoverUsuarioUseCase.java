package br.com.empresa.metas.usuario.core.usecase;

import br.com.empresa.metas.usuario.core.domain.gateway.UsuarioGateway;

public class RemoverUsuarioUseCase {

    private final UsuarioGateway usuarioGateway;

    public RemoverUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    /** Retorna true se removeu, false se o CPF não existia. */
    public boolean executar(String cpf) {
        return usuarioGateway.remover(cpf);
    }
}
