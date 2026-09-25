package br.com.empresa.metas.usuario.infra.controller.dto.response;

import br.com.empresa.metas.usuario.core.domain.model.Usuario;

import java.time.Instant;

public record UsuarioResponse(
        String cpf,
        String nome,
        String cargo,
        boolean admin,
        Instant dataCadastro
) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getCpf(),
                usuario.getNome(),
                usuario.getCargo(),
                usuario.isAdmin(),
                usuario.getDataCadastro()
        );
    }
}
