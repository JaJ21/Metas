import "server-only";
import { apiClient } from "@/lib/http/api-client";
import type { Permissao } from "../../domain/entities/permissao";

export async function listarPermissoes(token: string): Promise<Permissao[]> {
  return apiClient<Permissao[]>("/permissoes", { token });
}

export async function salvarPermissao(token: string, input: Permissao): Promise<Permissao> {
  return apiClient<Permissao>("/permissoes", { method: "POST", token, body: input });
}

export async function removerPermissao(token: string, cpf: string, centroCusto: string, codConta: string): Promise<void> {
  const query = new URLSearchParams({ cpf, centroCusto, codConta }).toString();
  await apiClient<void>(`/permissoes?${query}`, { method: "DELETE", token });
}
