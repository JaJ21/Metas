package br.com.empresa.metas.permissao.core.usecase;

import br.com.empresa.metas.permissao.core.domain.gateway.PermissaoGateway;
import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;
import br.com.empresa.metas.permissao.core.domain.model.Permissao;

/** Cria uma permissão nova, ou atualiza (upsert) se já existir uma para esse CPF + par. */
public class SalvarPermissaoUseCase {

    private final PermissaoGateway permissaoGateway;

    public SalvarPermissaoUseCase(PermissaoGateway permissaoGateway) {
        this.permissaoGateway = permissaoGateway;
    }

    public Permissao executar(String cpf, String centroCusto, String codConta, boolean read, boolean write, boolean delete) {
        ChaveCentroConta chave = new ChaveCentroConta(centroCusto, codConta);

        Permissao permissao = permissaoGateway.buscar(cpf, chave)
                .orElseGet(() -> new Permissao(cpf, chave, read, write, delete));
        permissao.atualizar(read, write, delete);

        return permissaoGateway.salvar(permissao);
    }
}
