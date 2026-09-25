package br.com.empresa.metas.permissao.core.usecase;

import br.com.empresa.metas.permissao.core.domain.gateway.PermissaoGateway;
import br.com.empresa.metas.permissao.core.domain.model.Permissao;

import java.util.List;

public class ListarPermissoesUseCase {

    private final PermissaoGateway permissaoGateway;

    public ListarPermissoesUseCase(PermissaoGateway permissaoGateway) {
        this.permissaoGateway = permissaoGateway;
    }

    public List<Permissao> executar() {
        return permissaoGateway.listarTodas();
    }
}
