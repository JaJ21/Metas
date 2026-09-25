package br.com.empresa.metas.permissao.core.usecase;

import br.com.empresa.metas.permissao.core.domain.gateway.PermissaoGateway;
import br.com.empresa.metas.permissao.core.domain.model.Permissao;
import br.com.empresa.metas.permissao.core.exception.PlanilhaPermissaoInvalidaException;

import java.util.List;

/**
 * Substitui TODA a tabela de permissões pela lista enviada (mesmo
 * comportamento do permissoes.importar_planilha do sistema em Python —
 * intencionalmente destrutivo, avisado na documentação da API).
 */
public class ImportarPermissoesUseCase {

    private final PermissaoGateway permissaoGateway;

    public ImportarPermissoesUseCase(PermissaoGateway permissaoGateway) {
        this.permissaoGateway = permissaoGateway;
    }

    public int executar(List<Permissao> novasPermissoes) {
        if (novasPermissoes == null || novasPermissoes.isEmpty()) {
            throw new PlanilhaPermissaoInvalidaException("A planilha de permissões está vazia.");
        }
        permissaoGateway.substituirTudo(novasPermissoes);
        return novasPermissoes.size();
    }
}
