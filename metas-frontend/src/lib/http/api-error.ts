import type { ApiProblem } from "@/contracts/api/problem";

/**
 * Exceção lançada pelo cliente HTTP quando a API responde com erro.
 * Carrega o ApiProblem inteiro (RFC 7807) pra quem capturar decidir o
 * que fazer — normalmente, mostrar problem.displayMessage na tela.
 */
export class ApiError extends Error {
  readonly problem: ApiProblem;

  constructor(problem: ApiProblem) {
    super(problem.detail ?? problem.title);
    this.problem = problem;
    this.name = "ApiError";
  }
}
