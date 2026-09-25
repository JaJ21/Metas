/** Uma linha do preview — mesmo shape que MetaPreviewResponse do backend, já em português/camelCase de domínio do front. */
export type LinhaMeta = {
  centroCusto: string;
  codConta: string;
  real: Record<number, number>;
  forecast: Record<number, number>;
  orcado: Record<number, number>;
  totalForecast: number;
  totalOrcado: number;
  justificativa: string;
  totaisAtuais: {
    existiaAntes: boolean;
    totalReal: number;
    totalForecast: number;
    totalOrcado: number;
  } | null;
};

export type PreviewPlanilha = {
  ciclo: import("../value-objects/ciclo-orcamentario").CicloOrcamentario;
  linhas: LinhaMeta[];
};
