package br.com.empresa.metas.meta.core.domain.model;

import br.com.empresa.metas.meta.core.domain.enums.TipoValorMes;

import java.math.BigDecimal;

/** Um mês já validado e normalizado — valor sempre um BigDecimal de verdade. */
public record ValorMensal(int ano, int mes, TipoValorMes tipo, BigDecimal valor) {
}
