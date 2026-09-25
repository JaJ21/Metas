package br.com.empresa.metas.permissao.core.domain.gateway;

import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;
import br.com.empresa.metas.permissao.core.domain.model.Permissao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissaoGateway {

    List<Permissao> listarTodas();

    Optional<Permissao> buscar(String cpf, ChaveCentroConta chave);

    Permissao salvar(Permissao permissao);

    boolean remover(String cpf, ChaveCentroConta chave);

    /** Pares (Centro de Custo, Cod Conta) em que esse CPF tem permissão de escrita (write=true). */
    Set<ChaveCentroConta> paresComPermissaoDeEscrita(String cpf);

    /** Pares em que esse CPF tem permissão de leitura (read=true). */
    Set<ChaveCentroConta> paresComPermissaoDeLeitura(String cpf);

    /** Apaga TUDO e grava a lista nova no lugar — usado pela importação de planilha. */
    void substituirTudo(List<Permissao> novasPermissoes);
}
