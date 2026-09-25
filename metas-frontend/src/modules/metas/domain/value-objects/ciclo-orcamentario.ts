/**
 * Espelha br.com.empresa.metas.meta.core.domain.model.CicloOrcamentario
 * do backend — o front não decide sozinho quais meses são REAL,
 * FORECAST ou ORÇADO; ele recebe isso do servidor a cada preview,
 * pra nunca ficar dessincronizado se o backend mudar o ciclo.
 */
export type CicloOrcamentario = {
  anoRealizado: number;
  anoOrcamento: number;
  mesCorteReal: number;
};

const NOMES_MESES = ["Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"] as const;

export function mesesReal(ciclo: CicloOrcamentario) {
  return Array.from({ length: ciclo.mesCorteReal }, (_, i) => ({
    mes: i + 1,
    ano: ciclo.anoRealizado,
    rotulo: `${NOMES_MESES[i]}/${ciclo.anoRealizado}`,
  }));
}

export function mesesForecast(ciclo: CicloOrcamentario) {
  return Array.from({ length: 12 - ciclo.mesCorteReal }, (_, i) => {
    const mes = ciclo.mesCorteReal + i + 1;
    return { mes, ano: ciclo.anoRealizado, rotulo: `${NOMES_MESES[mes - 1]}/${ciclo.anoRealizado}` };
  });
}

export function mesesOrcado(ciclo: CicloOrcamentario) {
  return Array.from({ length: 12 }, (_, i) => ({
    mes: i + 1,
    ano: ciclo.anoOrcamento,
    rotulo: `${NOMES_MESES[i]}/${ciclo.anoOrcamento}`,
  }));
}
