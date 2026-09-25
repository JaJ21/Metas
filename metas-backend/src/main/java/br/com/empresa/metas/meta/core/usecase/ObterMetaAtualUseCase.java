package br.com.empresa.metas.meta.core.usecase;

import br.com.empresa.metas.meta.core.domain.gateway.MetaGateway;
import br.com.empresa.metas.meta.core.domain.model.Meta;

import java.util.List;
import java.util.Optional;

/** Usado pela rota de "baixar último upload de novo", que busca os valores ATUAIS dos pares que fizeram parte daquele upload. */
public class ObterMetaAtualUseCase {

    private final MetaGateway metaGateway;

    public ObterMetaAtualUseCase(MetaGateway metaGateway) {
        this.metaGateway = metaGateway;
    }

    public List<Meta> executar(List<br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta> pares) {
        return pares.stream()
                .map(par -> metaGateway.buscar(par.centroCusto(), par.codConta()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
