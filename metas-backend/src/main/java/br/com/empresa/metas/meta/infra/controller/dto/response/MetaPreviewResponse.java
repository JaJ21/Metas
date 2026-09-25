package br.com.empresa.metas.meta.infra.controller.dto.response;

import br.com.empresa.metas.meta.core.domain.enums.TipoValorMes;
import br.com.empresa.metas.meta.core.usecase.PreviewLinha;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

public record MetaPreviewResponse(
        String centroCusto,
        String codConta,
        Map<Integer, BigDecimal> real,
        Map<Integer, BigDecimal> forecast,
        Map<Integer, BigDecimal> orcado,
        BigDecimal totalForecast,
        BigDecimal totalOrcado,
        String justificativa,
        TotaisAtuaisResponse totaisAtuais
) {
    public record TotaisAtuaisResponse(boolean existiaAntes, BigDecimal totalReal, BigDecimal totalForecast, BigDecimal totalOrcado) {}

    public static MetaPreviewResponse from(PreviewLinha linha) {
        var meta = linha.meta();

        Map<Integer, BigDecimal> real = mapaPorTipo(meta, TipoValorMes.REAL);
        Map<Integer, BigDecimal> forecast = mapaPorTipo(meta, TipoValorMes.FORECAST);
        Map<Integer, BigDecimal> orcado = mapaPorTipo(meta, TipoValorMes.ORCADO);

        TotaisAtuaisResponse totaisAtuais = linha.totaisAtuais() == null ? null : new TotaisAtuaisResponse(
                linha.totaisAtuais().existiaAntes(),
                linha.totaisAtuais().totalReal(),
                linha.totaisAtuais().totalForecast(),
                linha.totaisAtuais().totalOrcado()
        );

        return new MetaPreviewResponse(
                meta.getCentroCusto(), meta.getCodConta(),
                real, forecast, orcado,
                meta.totalPorTipo(TipoValorMes.FORECAST), meta.totalPorTipo(TipoValorMes.ORCADO),
                meta.getJustificativa(), totaisAtuais
        );
    }

    private static Map<Integer, BigDecimal> mapaPorTipo(br.com.empresa.metas.meta.core.domain.model.Meta meta, TipoValorMes tipo) {
        return meta.getValores().stream()
                .filter(v -> v.tipo() == tipo)
                .collect(Collectors.toMap(v -> v.mes(), v -> v.valor()));
    }
}
