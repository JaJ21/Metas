package br.com.empresa.metas.permissao.infra.controller.dto.response;

import br.com.empresa.metas.permissao.core.domain.model.Permissao;

public record PermissaoResponse(
        String cpf,
        String centroCusto,
        String codConta,
        boolean read,
        boolean write,
        boolean delete
) {
    public static PermissaoResponse from(Permissao p) {
        return new PermissaoResponse(p.getCpf(), p.getChave().centroCusto(), p.getChave().codConta(), p.isRead(), p.isWrite(), p.isDelete());
    }
}
