package br.com.empresa.metas.meta.core.usecase;

import br.com.empresa.metas.meta.core.domain.gateway.MetaGateway;
import br.com.empresa.metas.meta.core.domain.gateway.UltimoUploadGateway;
import br.com.empresa.metas.meta.core.domain.model.CicloOrcamentario;
import br.com.empresa.metas.meta.core.domain.model.LinhaMetaInput;
import br.com.empresa.metas.meta.core.domain.model.Meta;
import br.com.empresa.metas.meta.core.domain.model.UltimoUpload;
import br.com.empresa.metas.meta.core.domain.policy.ValidacaoPlanilhaPolicy;
import br.com.empresa.metas.permissao.core.domain.model.ChaveCentroConta;
import br.com.empresa.metas.permissao.core.usecase.VerificarPermissaoDeEscritaUseCase;
import br.com.empresa.metas.shared.core.exception.ForbiddenException;

import java.time.Instant;
import java.util.List;

/**
 * Caso de uso: usuário clicou em "Confirmar e salvar" no preview.
 * Equivalente à rota "/preview/confirmar" do sistema em Python.
 *
 * Valida de novo (os dados podem ter sido editados na tela desde o
 * preview), confere permissão de novo (o par pode ter mudado), aplica o
 * upsert no banco, e registra esse upload como o "último" desse CPF.
 */
public class ConfirmarUploadUseCase {

    private final MetaGateway metaGateway;
    private final UltimoUploadGateway ultimoUploadGateway;
    private final VerificarPermissaoDeEscritaUseCase verificarPermissaoDeEscritaUseCase;

    public ConfirmarUploadUseCase(
            MetaGateway metaGateway,
            UltimoUploadGateway ultimoUploadGateway,
            VerificarPermissaoDeEscritaUseCase verificarPermissaoDeEscritaUseCase
    ) {
        this.metaGateway = metaGateway;
        this.ultimoUploadGateway = ultimoUploadGateway;
        this.verificarPermissaoDeEscritaUseCase = verificarPermissaoDeEscritaUseCase;
    }

    public record Resumo(int substituidas, int novas) {}

    public Resumo executar(List<LinhaMetaInput> linhasEditadas, CicloOrcamentario ciclo, String cpfLogado, String nomeArquivoOriginal) {
        ValidacaoPlanilhaPolicy.validarSemDuplicados(linhasEditadas);

        List<Meta> metasValidadas = linhasEditadas.stream()
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

        MetaGateway.ResultadoUpsert resultado = metaGateway.salvarOuAtualizar(metasValidadas, cpfLogado);

        ultimoUploadGateway.registrar(new UltimoUpload(
                cpfLogado,
                nomeArquivoOriginal,
                Instant.now(),
                metasValidadas.size(),
                resultado.substituidas(),
                resultado.novas(),
                pares
        ));

        return new Resumo(resultado.substituidas(), resultado.novas());
    }
}
