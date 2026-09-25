package br.com.empresa.metas.meta.core.domain.model;

/**
 * Um mês ainda "cru" — o valor pode vir com vírgula decimal, "-" no
 * lugar de zero, espaços, etc (o mesmo formato bagunçado que uma planilha
 * ou um formulário HTML mandam). É isso que a camada core VALIDA e
 * transforma num ValorMensal (com BigDecimal de verdade).
 */
public record ValorMesInput(int ano, int mes, String valorBruto) {
}
