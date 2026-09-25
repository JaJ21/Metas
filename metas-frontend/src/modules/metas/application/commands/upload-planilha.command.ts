import { uploadPlanilha } from "../../infrastructure/api/metas-api";
import type { PreviewPlanilha } from "../../domain/entities/linha-meta";

/**
 * Um Command bem fino: a validação "de verdade" (sem letras, "-" vira
 * zero, permissão de escrita) acontece no backend — não faz sentido
 * duplicar aqui. O papel deste command é só ser o ÚNICO ponto de
 * entrada dessa operação pro resto da aplicação (a Route Handler nunca
 * chama a infraestrutura direto).
 */
export async function uploadPlanilhaCommand(token: string, formData: FormData): Promise<PreviewPlanilha> {
  return uploadPlanilha(token, formData);
}
