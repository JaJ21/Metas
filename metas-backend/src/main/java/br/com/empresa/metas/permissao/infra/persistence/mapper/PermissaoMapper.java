package br.com.empresa.metas.permissao.infra.persistence.mapper;

import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;
import br.com.empresa.metas.permissao.core.domain.model.Permissao;
import br.com.empresa.metas.permissao.infra.persistence.entity.PermissaoEntity;

public final class PermissaoMapper {

    private PermissaoMapper() {}

    public static Permissao toDomain(PermissaoEntity entity) {
        return new Permissao(
                entity.getCpf(),
                new ChaveCentroConta(entity.getCentroCusto(), entity.getCodConta()),
                entity.isLeitura(),
                entity.isEscrita(),
                entity.isExclusao()
        );
    }

    public static PermissaoEntity toEntity(Permissao domain, Long idExistente) {
        return new PermissaoEntity(
                idExistente,
                domain.getCpf(),
                domain.getChave().centroCusto(),
                domain.getChave().codConta(),
                domain.isRead(),
                domain.isWrite(),
                domain.isDelete()
        );
    }
}
