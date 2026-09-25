package br.com.empresa.metas.meta.infra.persistence.mapper;

import br.com.empresa.metas.meta.core.domain.enums.TipoValorMes;
import br.com.empresa.metas.meta.core.domain.model.Meta;
import br.com.empresa.metas.meta.core.domain.model.ValorMensal;
import br.com.empresa.metas.meta.infra.persistence.entity.MetaEntity;
import br.com.empresa.metas.meta.infra.persistence.entity.MetaValorEntity;

import java.util.List;

public final class MetaMapper {

    private MetaMapper() {}

    public static Meta toDomain(MetaEntity entity, List<MetaValorEntity> valoresEntity) {
        List<ValorMensal> valores = valoresEntity.stream()
                .map(v -> new ValorMensal(v.getAno(), v.getMes(), TipoValorMes.valueOf(v.getTipo()), v.getValor()))
                .toList();

        return new Meta(
                entity.getCentroCusto(),
                entity.getCodConta(),
                valores,
                entity.getJustificativa(),
                entity.getCpfUltimaAlteracao(),
                entity.getDataUltimaAlteracao()
        );
    }
}
