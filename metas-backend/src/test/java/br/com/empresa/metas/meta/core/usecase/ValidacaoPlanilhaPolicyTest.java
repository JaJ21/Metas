package br.com.empresa.metas.meta.core.usecase;

import br.com.empresa.metas.meta.core.domain.enums.TipoValorMes;
import br.com.empresa.metas.meta.core.domain.model.CicloOrcamentario;
import br.com.empresa.metas.meta.core.domain.model.LinhaMetaInput;
import br.com.empresa.metas.meta.core.domain.model.ValorMesInput;
import br.com.empresa.metas.meta.core.domain.policy.ValidacaoPlanilhaPolicy;
import br.com.empresa.metas.shared.core.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Teste de UNIDADE do core — não sobe Spring, não toca banco, roda em
 * milissegundos. É o tipo de teste que o padrão de arquitetura pede pra
 * cada caso de uso/policy: prova que a regra de negócio funciona
 * isoladamente, sem depender de infraestrutura nenhuma.
 */
class ValidacaoPlanilhaPolicyTest {

    private final CicloOrcamentario ciclo = new CicloOrcamentario(2026, 2027, 7);

    @Test
    void deve_tratar_traco_e_vazio_como_zero() {
        LinhaMetaInput input = new LinhaMetaInput("1001", "30001",
                List.of(new ValorMesInput(2026, 1, "-"), new ValorMesInput(2026, 2, "")),
                "");

        var meta = ValidacaoPlanilhaPolicy.validarENormalizar(input, ciclo);

        assertThat(meta.getValores().get(0).valor()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(meta.getValores().get(1).valor()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void deve_aceitar_virgula_como_separador_decimal() {
        LinhaMetaInput input = new LinhaMetaInput("1001", "30001",
                List.of(new ValorMesInput(2026, 8, "1234,56")), "");

        var meta = ValidacaoPlanilhaPolicy.validarENormalizar(input, ciclo);

        assertThat(meta.getValores().get(0).valor()).isEqualByComparingTo(new BigDecimal("1234.56"));
        assertThat(meta.getValores().get(0).tipo()).isEqualTo(TipoValorMes.FORECAST);
    }

    @Test
    void deve_rejeitar_letra_fora_da_justificativa() {
        LinhaMetaInput input = new LinhaMetaInput("ABC", "30001", List.of(), "");

        assertThatThrownBy(() -> ValidacaoPlanilhaPolicy.validarENormalizar(input, ciclo))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void deve_permitir_letra_na_justificativa() {
        LinhaMetaInput input = new LinhaMetaInput("1001", "30001", List.of(), "Aumento de custo com aluguel");

        var meta = ValidacaoPlanilhaPolicy.validarENormalizar(input, ciclo);

        assertThat(meta.getJustificativa()).isEqualTo("Aumento de custo com aluguel");
    }

    @Test
    void deve_classificar_mes_como_real_forecast_ou_orcado_conforme_o_ciclo() {
        assertThat(ciclo.classificar(2026, 3)).isEqualTo(TipoValorMes.REAL);
        assertThat(ciclo.classificar(2026, 9)).isEqualTo(TipoValorMes.FORECAST);
        assertThat(ciclo.classificar(2027, 1)).isEqualTo(TipoValorMes.ORCADO);
    }

    @Test
    void deve_rejeitar_linhas_duplicadas_na_mesma_planilha() {
        List<LinhaMetaInput> linhas = List.of(
                new LinhaMetaInput("1001", "30001", List.of(), ""),
                new LinhaMetaInput("1001", "30001", List.of(), "")
        );

        assertThatThrownBy(() -> ValidacaoPlanilhaPolicy.validarSemDuplicados(linhas))
                .isInstanceOf(ValidationException.class);
    }
}
