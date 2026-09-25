import type { LinhaMeta } from "../../domain/entities/linha-meta";

/**
 * Estado de EDIÇÃO de uma linha — diferente da entidade LinhaMeta (que
 * é o que veio do servidor). Guarda os valores como STRING (o que o
 * usuário está digitando no input pode não ser um número válido ainda
 * no meio da digitação), seguindo a seção 5.4/13 do documento: "o
 * formulário mantém um estado próprio com valor atual, valor original
 * e campos alterados".
 */
export type LinhaEditavel = {
  centroCusto: string;
  codConta: string;
  real: Record<number, number>; // não editável — só exibição
  forecast: Record<number, string>;
  orcado: Record<number, string>;
  justificativa: string;
  totaisAtuais: LinhaMeta["totaisAtuais"];
};

export function paraLinhaEditavel(linha: LinhaMeta): LinhaEditavel {
  return {
    centroCusto: linha.centroCusto,
    codConta: linha.codConta,
    real: linha.real,
    forecast: mapParaString(linha.forecast),
    orcado: mapParaString(linha.orcado),
    justificativa: linha.justificativa,
    totaisAtuais: linha.totaisAtuais,
  };
}

function mapParaString(mapa: Record<number, number>): Record<number, string> {
  return Object.fromEntries(Object.entries(mapa).map(([mes, valor]) => [mes, String(valor)]));
}

export function somar(valores: Record<number, string>): number {
  return Object.values(valores).reduce((total, valor) => {
    const numero = parseFloat(valor.replace(",", "."));
    return total + (Number.isFinite(numero) ? numero : 0);
  }, 0);
}
