/**
 * Formato interno único de paginação — a infraestrutura de cada módulo
 * é responsável por normalizar o que a API externa devolve (que pode
 * ter um shape ligeiramente diferente) pra este formato, então as telas
 * nunca precisam saber a diferença.
 */
export type PageResult<T> = {
  items: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  hasNext: boolean;
};

export function emptyPageResult<T>(): PageResult<T> {
  return { items: [], page: 0, size: 0, totalElements: 0, totalPages: 0, hasNext: false };
}
