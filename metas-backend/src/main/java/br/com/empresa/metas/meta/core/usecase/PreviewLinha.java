package br.com.empresa.metas.meta.core.usecase;

import br.com.empresa.metas.meta.core.domain.model.Meta;

/**
 * Uma linha do preview: a Meta validada + (se o usuário tiver permissão
 * de Read) os totais que JÁ ESTAVAM salvos no banco pra esse par, pra
 * comparação. "totaisAtuais" null significa "sem permissão de leitura"
 * (equivalente ao texto "sem permissão de leitura" mostrado no preview
 * do sistema em Python).
 */
public record PreviewLinha(Meta meta, TotaisAtuais totaisAtuais) {

    public record TotaisAtuais(boolean existiaAntes, java.math.BigDecimal totalReal,
                                 java.math.BigDecimal totalForecast, java.math.BigDecimal totalOrcado) {}
}
