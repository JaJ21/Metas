import { z } from "zod";
import { salvarUsuario } from "../../infrastructure/api/usuarios-api";

export const salvarUsuarioInputSchema = z.object({
  cpf: z.string().regex(/^\d{11}$/, "CPF deve ter 11 dígitos"),
  nome: z.string().min(1, "Informe o nome"),
  cargo: z.string().optional().default(""),
  senha: z.string().optional().default(""),
  admin: z.boolean().default(false),
});

export async function salvarUsuarioCommand(token: string, input: unknown) {
  const dados = salvarUsuarioInputSchema.parse(input);
  return salvarUsuario(token, dados);
}
