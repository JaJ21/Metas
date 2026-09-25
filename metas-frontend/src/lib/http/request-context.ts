import "server-only";
import { randomUUID } from "node:crypto";

/**
 * Identificador de correlação — gerado uma vez por requisição
 * server-side e propagado no header "X-Correlation-Id" pra toda
 * chamada feita ao backend. Isso permite, quando algo dá errado,
 * encontrar o mesmo pedido nos logs dos dois lados (front e backend).
 */
export function novoIdDeCorrelacao(): string {
  return randomUUID();
}
