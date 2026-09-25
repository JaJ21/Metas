package br.com.empresa.metas.meta.core.domain.model;

import java.util.List;

/**
 * Uma linha "crua" recebida (seja de um arquivo .xlsx recém-enviado, seja
 * dos campos editados na tela de preview) — ainda não validada. Entra
 * como esta classe no ValidacaoPlanilhaPolicy e sai como uma Meta
 * validada e normalizada.
 */
public record LinhaMetaInput(
        String centroCusto,
        String codConta,
        List<ValorMesInput> valores,
        String justificativaBruta
) {
}
