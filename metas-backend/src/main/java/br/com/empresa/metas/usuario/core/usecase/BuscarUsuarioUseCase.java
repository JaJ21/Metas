package br.com.empresa.metas.usuario.core.usecase;

import br.com.empresa.metas.usuario.core.domain.gateway.UsuarioGateway;
import br.com.empresa.metas.usuario.core.domain.model.Usuario;
import br.com.empresa.metas.usuario.core.exception.UsuarioNaoEncontradoException;

public class BuscarUsuarioUseCase {

    private final UsuarioGateway usuarioGateway;

    public BuscarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario executar(String cpf) {
        return usuarioGateway.buscarPorCpf(cpf)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(cpf));
    }
}
