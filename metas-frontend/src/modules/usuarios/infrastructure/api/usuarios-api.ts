import "server-only";
import { apiClient } from "@/lib/http/api-client";
import type { Usuario } from "../../domain/entities/usuario";

export async function listarUsuarios(token: string): Promise<Usuario[]> {
  return apiClient<Usuario[]>("/usuarios", { token });
}

export async function salvarUsuario(token: string, input: {
  cpf: string; nome: string; cargo: string; senha: string; admin: boolean;
}): Promise<Usuario> {
  return apiClient<Usuario>("/usuarios", { method: "POST", token, body: input });
}

export async function removerUsuario(token: string, cpf: string): Promise<void> {
  await apiClient<void>(`/usuarios/${cpf}`, { method: "DELETE", token });
}
