package br.com.empresa.metas.usuario.core.usecase;

import br.com.empresa.metas.usuario.core.domain.gateway.UsuarioGateway;
import br.com.empresa.metas.usuario.core.domain.model.Usuario;

import java.util.List;

public class ListarUsuariosUseCase {

    private final UsuarioGateway usuarioGateway;

    public ListarUsuariosUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public List<Usuario> executar() {
        return usuarioGateway.listarTodos();
    }
}
