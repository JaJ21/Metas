import { z } from "zod";

// Schema único de validação das variáveis de ambiente — se faltar
// alguma, a aplicação falha JÁ NO BOOT (não em produção, na hora que
// alguém tentar usar a rota que precisava dela). Isso é o que o
// documento de arquitetura pede na seção 18 ("Configuração e ambientes").
export const serverEnvSchema = z.object({
  METAS_API_URL: z.string().url(),
  SESSION_COOKIE_NAME: z.string().min(1).default("metas_session"),
  SESSION_COOKIE_SECURE: z
    .string()
    .default("false")
    .transform((v) => v === "true"),
});

export type ServerEnv = z.infer<typeof serverEnvSchema>;
