package br.com.empresa.metas.meta.core.domain.gateway;

import br.com.empresa.metas.meta.core.domain.model.Meta;

import java.util.List;
import java.util.Optional;

public interface MetaGateway {

    Optional<Meta> buscar(String centroCusto, String codConta);

    record ResultadoUpsert(int substituidas, int novas) {}

    /**
     * Aplica o "upsert" de todas as metas da lista, de uma vez (mesma
     * semântica do aplicar_upsert() do excel_utils.py original): cada
     * par Centro de Custo + Cod Conta que já existir é SUBSTITUÍDO,
     * o que não existir é INSERIDO.
     */
    ResultadoUpsert salvarOuAtualizar(List<Meta> metas, String cpf);
}
