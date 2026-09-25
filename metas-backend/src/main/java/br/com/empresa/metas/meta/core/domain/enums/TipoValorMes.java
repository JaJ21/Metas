package br.com.empresa.metas.meta.core.domain.enums;

/**
 * Os três "blocos" de mês que existem na planilha de metas — equivalente
 * a MESES_REAL / MESES_FORECAST / MESES_ORCADO do config.py original.
 *
 *   REAL      -> já aconteceu, só referência (não editável)
 *   FORECAST  -> meses restantes do ano corrente (editável)
 *   ORCADO    -> os 12 meses do próximo ano (editável)
 */
public enum TipoValorMes {
    REAL, FORECAST, ORCADO
}
