package br.com.empresa.metas.meta.core.domain.model;

import br.com.empresa.metas.meta.core.domain.enums.TipoValorMes;

/**
 * Objeto de domínio que sabe classificar "esse mês/ano é REAL, FORECAST
 * ou ORÇADO?" — é o equivalente, em Java, das variáveis ANO_REALIZADO /
 * ANO_ORCAMENTO / MES_CORTE_REAL do config.py original. A diferença é
 * que aqui isso vira um objeto de domínio de verdade (com comportamento,
 * não só constantes soltas), porque essa classificação é uma REGRA DE
 * NEGÓCIO, não um detalhe técnico.
 *
 * Os valores (anoRealizado, anoOrcamento, mesCorteReal) vêm de
 * application.yml — quem lê o @Value e monta esse objeto é a camada
 * infra (MetaUseCaseConfig), o core só recebe o objeto já pronto.
 */
public record CicloOrcamentario(int anoRealizado, int anoOrcamento, int mesCorteReal) {

    public TipoValorMes classificar(int ano, int mes) {
        if (ano == anoRealizado && mes <= mesCorteReal) {
            return TipoValorMes.REAL;
        }
        if (ano == anoRealizado && mes > mesCorteReal) {
            return TipoValorMes.FORECAST;
        }
        if (ano == anoOrcamento) {
            return TipoValorMes.ORCADO;
        }
        throw new IllegalArgumentException("Mês/ano fora do ciclo orçamentário atual: " + mes + "/" + ano);
    }
}
