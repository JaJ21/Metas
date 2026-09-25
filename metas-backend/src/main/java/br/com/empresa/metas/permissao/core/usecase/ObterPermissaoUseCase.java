package br.com.empresa.metas.permissao.core.usecase;

import br.com.empresa.metas.permissao.core.domain.gateway.PermissaoGateway;
import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;
import br.com.empresa.metas.permissao.core.domain.model.Permissao;

/** Usado no preview do módulo "meta" pra decidir se mostra o valor atual do master (permissão Read). */
public class ObterPermissaoUseCase {

    private final PermissaoGateway permissaoGateway;

    public ObterPermissaoUseCase(PermissaoGateway permissaoGateway) {
        this.permissaoGateway = permissaoGateway;
    }

    public Permissao executar(String cpf, String centroCusto, String codConta) {
        ChaveCentroConta chave = new ChaveCentroConta(centroCusto, codConta);
        return permissaoGateway.buscar(cpf, chave)
                .orElseGet(() -> new Permissao(cpf, chave, false, false, false));
    }
}
