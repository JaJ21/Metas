package br.com.empresa.metas.usuario.core.exception;

import br.com.empresa.metas.shared.core.exception.NotFoundException;

public class UsuarioNaoEncontradoException extends NotFoundException {
    public UsuarioNaoEncontradoException(String cpf) {
        super("USUARIO_NAO_ENCONTRADO", "Usuário com CPF " + cpf + " não encontrado.");
    }
}
