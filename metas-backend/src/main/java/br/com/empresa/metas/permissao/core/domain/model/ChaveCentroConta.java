package br.com.empresa.metas.permissao.core.domain.model;

/**
 * "Value object": representa a combinação Centro de Custo + Cod Conta
 * que identifica uma linha de metas. É um "record" (imutável, comparável
 * por valor — dois ChaveCentroConta com os mesmos dados são == iguais
 * pra fins de equals/hashCode, o que é importante pra usar em Set/Map).
 * Usado tanto no módulo permissao quanto no módulo meta.
 */
public record ChaveCentroConta(String centroCusto, String codConta) {
}
