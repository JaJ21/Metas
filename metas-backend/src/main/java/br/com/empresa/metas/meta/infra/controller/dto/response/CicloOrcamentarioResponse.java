package br.com.empresa.metas.meta.infra.controller.dto.response;

import br.com.empresa.metas.meta.core.domain.model.CicloOrcamentario;

/**
 * Informa ao front-end qual é o ciclo orçamentário atual (anos e mês de
 * corte), pra ele conseguir montar os rótulos de coluna (Jan/2026 etc)
 * sem precisar "adivinhar" ou duplicar essa regra.
 */
public record CicloOrcamentarioResponse(int anoRealizado, int anoOrcamento, int mesCorteReal) {
    public static CicloOrcamentarioResponse from(CicloOrcamentario ciclo) {
        return new CicloOrcamentarioResponse(ciclo.anoRealizado(), ciclo.anoOrcamento(), ciclo.mesCorteReal());
    }
}
