/**
 * Formato de erro padronizado (RFC 7807 "Problem Details for HTTP
 * APIs") — o mesmo formato que o GlobalExceptionHandler do backend
 * Java devolve. Qualquer erro da API cai nesse shape, então a interface
 * trata erro de qualquer módulo do mesmo jeito.
 */
export type ApiProblem = {
  type?: string;
  title: string;
  status: number;
  code?: string;
  detail?: string;
  displayMessage?: string;
  instance?: string;
  timestamp?: string;
};

export function isApiProblem(value: unknown): value is ApiProblem {
  return (
    typeof value === "object" &&
    value !== null &&
    "status" in value &&
    "title" in value
  );
}

/** Mensagem segura pra mostrar na tela — nunca stack trace nem detalhe técnico interno. */
export function displayMessageOf(problem: ApiProblem): string {
  return problem.displayMessage ?? problem.detail ?? problem.title ?? "Ocorreu um erro inesperado.";
}
