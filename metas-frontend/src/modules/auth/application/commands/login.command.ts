import { z } from "zod";
import { efetuarLogin } from "../../infrastructure/api/auth-api";

/**
 * Um "Command" (seção 5.2 do documento de arquitetura): representa UMA
 * intenção de mutação, com entrada validada por schema e saída própria
 * da aplicação (nunca o JSON cru que a API externa devolveu).
 */
export const loginInputSchema = z.object({
  cpf: z.string().regex(/^\d{11}$/, "CPF deve ter 11 dígitos numéricos"),
  senha: z.string().min(1, "Informe a senha"),
});

export type LoginInput = z.infer<typeof loginInputSchema>;

export type LoginResult = {
  token: string;
  cpf: string;
  nome: string;
  roles: string[];
};

export async function loginCommand(input: unknown): Promise<LoginResult> {
  const dadosValidados = loginInputSchema.parse(input);
  const resposta = await efetuarLogin(dadosValidados);

  return {
    token: resposta.token,
    cpf: resposta.cpf,
    nome: resposta.nome,
    roles: resposta.roles,
  };
}
