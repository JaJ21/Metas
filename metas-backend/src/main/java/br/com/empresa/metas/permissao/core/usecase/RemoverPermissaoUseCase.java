package br.com.empresa.metas.permissao.core.usecase;

import br.com.empresa.metas.permissao.core.domain.gateway.PermissaoGateway;
import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;

public class RemoverPermissaoUseCase {

    private final PermissaoGateway permissaoGateway;

    public RemoverPermissaoUseCase(PermissaoGateway permissaoGateway) {
        this.permissaoGateway = permissaoGateway;
    }

    public boolean executar(String cpf, String centroCusto, String codConta) {
        return permissaoGateway.remover(cpf, new ChaveCentroConta(centroCusto, codConta));
    }
}
