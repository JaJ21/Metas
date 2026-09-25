import { z } from "zod";
import { salvarPermissao } from "../../infrastructure/api/permissoes-api";

export const salvarPermissaoInputSchema = z.object({
  cpf: z.string().min(1),
  centroCusto: z.string().min(1),
  codConta: z.string().min(1),
  read: z.boolean().default(false),
  write: z.boolean().default(false),
  delete: z.boolean().default(false),
});

export async function salvarPermissaoCommand(token: string, input: unknown) {
  const dados = salvarPermissaoInputSchema.parse(input);
  return salvarPermissao(token, dados);
}
