import "server-only";
import { apiClient } from "@/lib/http/api-client";
import type { LoginInput } from "../../application/commands/login.command";

type LoginResponseDto = {
  token: string;
  tokenType: string;
  expiresInMinutes: number;
  cpf: string;
  nome: string;
  roles: string[];
};

/** Adaptador HTTP específico do módulo — só ele conhece o endpoint /auth/login do backend. */
export async function efetuarLogin(input: LoginInput): Promise<LoginResponseDto> {
  return apiClient<LoginResponseDto>("/auth/login", { method: "POST", body: input });
}
