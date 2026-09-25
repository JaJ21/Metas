package br.com.empresa.metas.permissao.core.usecase;

import br.com.empresa.metas.permissao.core.domain.gateway.PermissaoGateway;
import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Equivalente a permissoes.usuario_pode_escrever(...) do sistema em
 * Python: confere se o CPF tem permissão de Write para TODOS os pares
 * (Centro de Custo, Cod Conta) de uma lista — usada antes de aceitar um
 * upload de metas (módulo "meta").
 */
public class VerificarPermissaoDeEscritaUseCase {

    private final PermissaoGateway permissaoGateway;

    public VerificarPermissaoDeEscritaUseCase(PermissaoGateway permissaoGateway) {
        this.permissaoGateway = permissaoGateway;
    }

    public record Resultado(boolean pode, List<ChaveCentroConta> paresSemPermissao) {}

    public Resultado executar(String cpf, List<ChaveCentroConta> paresDaPlanilha) {
        Set<ChaveCentroConta> permitidos = permissaoGateway.paresComPermissaoDeEscrita(cpf);

        List<ChaveCentroConta> semPermissao = paresDaPlanilha.stream()
                .filter(par -> !permitidos.contains(par))
                .distinct()
                .collect(Collectors.toList());

        return new Resultado(semPermissao.isEmpty(), semPermissao);
    }
}
