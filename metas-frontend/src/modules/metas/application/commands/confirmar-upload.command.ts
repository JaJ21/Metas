import { z } from "zod";
import { confirmarUpload, type ResumoUploadDto } from "../../infrastructure/api/metas-api";

const linhaSchema = z.object({
  centroCusto: z.string().min(1),
  codConta: z.string().min(1),
  real: z.record(z.string(), z.string()).optional(),
  forecast: z.record(z.string(), z.string()).optional(),
  orcado: z.record(z.string(), z.string()).optional(),
  justificativa: z.string().optional(),
});

export const confirmarUploadInputSchema = z.object({
  nomeArquivoOriginal: z.string().optional(),
  linhas: z.array(linhaSchema).min(1),
});

export type ConfirmarUploadInput = z.infer<typeof confirmarUploadInputSchema>;

export async function confirmarUploadCommand(token: string, input: unknown): Promise<ResumoUploadDto> {
  const dadosValidados = confirmarUploadInputSchema.parse(input);
  return confirmarUpload(token, dadosValidados);
}
