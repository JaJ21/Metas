package br.com.empresa.metas.meta.core.usecase;

import br.com.empresa.metas.meta.core.domain.enums.TipoValorMes;
import br.com.empresa.metas.meta.core.domain.gateway.MetaGateway;
import br.com.empresa.metas.meta.core.domain.model.*;
import br.com.empresa.metas.meta.core.domain.policy.ValidacaoPlanilhaPolicy;
import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;
import br.com.empresa.metas.permissao.core.usecase.ObterPermissaoUseCase;
import br.com.empresa.metas.permissao.core.usecase.VerificarPermissaoDeEscritaUseCase;
import br.com.empresa.metas.shared.core.exception.ForbiddenException;

import java.util.List;

/**
 * Caso de uso: usuário acabou de subir/editar a planilha e quer ver o
 * PREVIEW (ainda não confirma nada no banco). Equivalente à rota
 * "/upload" (POST) do sistema em Python.
 *
 * Repare que este caso de uso do módulo "meta" usa CASOS DE USO do
 * módulo "permissao" (não a implementação infra, o core de outro
 * módulo) — é assim que módulos se comunicam na arquitetura hexagonal:
 * pela camada core, nunca pela infra um do outro.
 */
public class ImportarPlanilhaUseCase {

    private final MetaGateway metaGateway;
    private final VerificarPermissaoDeEscritaUseCase verificarPermissaoDeEscritaUseCase;
    private final ObterPermissaoUseCase obterPermissaoUseCase;

    public ImportarPlanilhaUseCase(
            MetaGateway metaGateway,
            VerificarPermissaoDeEscritaUseCase verificarPermissaoDeEscritaUseCase,
            ObterPermissaoUseCase obterPermissaoUseCase
    ) {
        this.metaGateway = metaGateway;
        this.verificarPermissaoDeEscritaUseCase = verificarPermissaoDeEscritaUseCase;
        this.obterPermissaoUseCase = obterPermissaoUseCase;
    }

    public List<PreviewLinha> executar(List<LinhaMetaInput> linhas, CicloOrcamentario ciclo, String cpfLogado) {
        ValidacaoPlanilhaPolicy.validarSemDuplicados(linhas);

        List<Meta> metasValidadas = linhas.stream()
                .map(linha -> ValidacaoPlanilhaPolicy.validarENormalizar(linha, ciclo))
                .toList();

        List<ChaveCentroConta> pares = metasValidadas.stream()
                .map(m -> new ChaveCentroConta(m.getCentroCusto(), m.getCodConta()))
                .toList();

        var resultadoPermissao = verificarPermissaoDeEscritaUseCase.executar(cpfLogado, pares);
        if (!resultadoPermissao.pode()) {
            throw new ForbiddenException("SEM_PERMISSAO_ESCRITA",
                    "Você não tem permissão de escrita para os pares: " + resultadoPermissao.paresSemPermissao());
        }

        return metasValidadas.stream()
                .map(meta -> montarPreviewLinha(meta, cpfLogado))
                .toList();
    }

    private PreviewLinha montarPreviewLinha(Meta meta, String cpfLogado) {
        var permissao = obterPermissaoUseCase.executar(cpfLogado, meta.getCentroCusto(), meta.getCodConta());
        if (!permissao.isRead()) {
            return new PreviewLinha(meta, null);
        }

        return metaGateway.buscar(meta.getCentroCusto(), meta.getCodConta())
                .map(atual -> new PreviewLinha(meta, new PreviewLinha.TotaisAtuais(
                        true,
                        atual.totalPorTipo(TipoValorMes.REAL),
                        atual.totalPorTipo(TipoValorMes.FORECAST),
                        atual.totalPorTipo(TipoValorMes.ORCADO)
                )))
                .orElseGet(() -> new PreviewLinha(meta, new PreviewLinha.TotaisAtuais(
                        false, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO
                )));
    }
}
