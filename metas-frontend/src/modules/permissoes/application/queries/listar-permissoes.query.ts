import { listarPermissoes } from "../../infrastructure/api/permissoes-api";

export async function listarPermissoesQuery(token: string) {
  return listarPermissoes(token);
}
